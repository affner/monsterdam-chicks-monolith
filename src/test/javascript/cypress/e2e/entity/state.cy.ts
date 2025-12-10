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

describe('State e2e test', () => {
  const statePageUrl = '/state';
  const statePageUrlPattern = new RegExp('/state(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const stateSample = {
    stateName: 'frightfully',
    isoCode: 'boo',
    createdDate: '2025-12-10T02:13:43.830Z',
    isDeleted: true,
    countryId: 23686,
  };

  let state;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/states+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/states').as('postEntityRequest');
    cy.intercept('DELETE', '/api/states/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (state) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/states/${state.id}`,
      }).then(() => {
        state = undefined;
      });
    }
  });

  it('States menu should load States page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('state');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('State').should('exist');
    cy.url().should('match', statePageUrlPattern);
  });

  describe('State page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(statePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create State page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/state/new$'));
        cy.getEntityCreateUpdateHeading('State');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/states',
          body: stateSample,
        }).then(({ body }) => {
          state = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/states+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/states?page=0&size=20>; rel="last",<http://localhost/api/states?page=0&size=20>; rel="first"',
              },
              body: [state],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(statePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details State page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('state');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });

      it('edit button click should load edit State page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('State');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });

      it('edit button click should load edit State page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('State');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });

      it('last delete button click should delete instance of State', () => {
        cy.intercept('GET', '/api/states/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('state').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);

        state = undefined;
      });
    });
  });

  describe('new State page', () => {
    beforeEach(() => {
      cy.visit(`${statePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('State');
    });

    it('should create an instance of State', () => {
      cy.get(`[data-cy="stateName"]`).type('recklessly spring zesty');
      cy.get(`[data-cy="stateName"]`).should('have.value', 'recklessly spring zesty');

      cy.get(`[data-cy="isoCode"]`).type('plu');
      cy.get(`[data-cy="isoCode"]`).should('have.value', 'plu');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T00:45');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T00:45');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T07:51');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T07:51');

      cy.get(`[data-cy="createdBy"]`).type('pause');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'pause');

      cy.get(`[data-cy="lastModifiedBy"]`).type('bewail');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'bewail');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="countryId"]`).type('4534');
      cy.get(`[data-cy="countryId"]`).should('have.value', '4534');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        state = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', statePageUrlPattern);
    });
  });
});
