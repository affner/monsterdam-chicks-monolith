import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('UserEvent e2e test', () => {
  const userEventPageUrl = '/user-event';
  const userEventPageUrlPattern = new RegExp('/user-event(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userEventSample = {
    title: 'whoever cook',
    description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    startDate: '2025-12-15',
    endDate: '2025-12-15',
    createdDate: '2025-12-15T11:29:23.746Z',
  };

  let userEvent;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-events+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-events').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-events/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userEvent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-events/${userEvent.id}`,
      }).then(() => {
        userEvent = undefined;
      });
    }
  });

  it('UserEvents menu should load UserEvents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-event');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserEvent').should('exist');
    cy.url().should('match', userEventPageUrlPattern);
  });

  describe('UserEvent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userEventPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserEvent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-event/new$'));
        cy.getEntityCreateUpdateHeading('UserEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-events',
          body: userEventSample,
        }).then(({ body }) => {
          userEvent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-events+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-events?page=0&size=20>; rel="last",<http://localhost/api/user-events?page=0&size=20>; rel="first"',
              },
              body: [userEvent],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userEventPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserEvent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userEvent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);
      });

      it('edit button click should load edit UserEvent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);
      });

      it('edit button click should load edit UserEvent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserEvent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);
      });

      it('last delete button click should delete instance of UserEvent', () => {
        cy.intercept('GET', '/api/user-events/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userEvent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userEventPageUrlPattern);

        userEvent = undefined;
      });
    });
  });

  describe('new UserEvent page', () => {
    beforeEach(() => {
      cy.visit(`${userEventPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserEvent');
    });

    it('should create an instance of UserEvent', () => {
      cy.get(`[data-cy="title"]`).type('vivacious rightfully unimpressively');
      cy.get(`[data-cy="title"]`).should('have.value', 'vivacious rightfully unimpressively');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="startDate"]`).type('2025-12-15');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2025-12-15');

      cy.get(`[data-cy="endDate"]`).type('2025-12-15');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2025-12-15');

      cy.get(`[data-cy="creatorEventStatus"]`).select('CANCELED');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T19:12');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T19:12');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-16T02:28');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-16T02:28');

      cy.get(`[data-cy="createdBy"]`).type('until playfully converse');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'until playfully converse');

      cy.get(`[data-cy="lastModifiedBy"]`).type('via');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'via');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T20:54');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T20:54');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userEvent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userEventPageUrlPattern);
    });
  });
});
