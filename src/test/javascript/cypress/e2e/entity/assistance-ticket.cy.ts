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

describe('AssistanceTicket e2e test', () => {
  const assistanceTicketPageUrl = '/assistance-ticket';
  const assistanceTicketPageUrlPattern = new RegExp('/assistance-ticket(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const assistanceTicketSample = {
    subject: 'although doting',
    description: 'carboxyl',
    status: 'CLOSED',
    type: 'REFUND_REQUEST',
    createdDate: '2025-12-16T06:53:29.284Z',
  };

  let assistanceTicket;
  let userLite;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-lites',
      body: {
        thumbnailS3Key: 'annually',
        birthDate: '2025-12-15',
        gender: 'FEMALE',
        createdDate: '2025-12-15T09:04:22.883Z',
        lastModifiedDate: '2025-12-15T22:30:36.214Z',
        createdBy: 'boo jealously chap',
        lastModifiedBy: 'woot but steep',
        deletedDate: '2025-12-15T13:36:24.932Z',
        nickName: '6mflpwethr',
        fullName: 'KLXug',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/assistance-tickets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/assistance-tickets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/assistance-tickets/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/platform-admin-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/identity-document-reviews', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
  });

  afterEach(() => {
    if (assistanceTicket) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assistance-tickets/${assistanceTicket.id}`,
      }).then(() => {
        assistanceTicket = undefined;
      });
    }
  });

  afterEach(() => {
    if (userLite) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-lites/${userLite.id}`,
      }).then(() => {
        userLite = undefined;
      });
    }
  });

  it('AssistanceTickets menu should load AssistanceTickets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('assistance-ticket');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssistanceTicket').should('exist');
    cy.url().should('match', assistanceTicketPageUrlPattern);
  });

  describe('AssistanceTicket page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assistanceTicketPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssistanceTicket page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/assistance-ticket/new$'));
        cy.getEntityCreateUpdateHeading('AssistanceTicket');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/assistance-tickets',
          body: {
            ...assistanceTicketSample,
            user: userLite,
          },
        }).then(({ body }) => {
          assistanceTicket = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/assistance-tickets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/assistance-tickets?page=0&size=20>; rel="last",<http://localhost/api/assistance-tickets?page=0&size=20>; rel="first"',
              },
              body: [assistanceTicket],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(assistanceTicketPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssistanceTicket page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assistanceTicket');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);
      });

      it('edit button click should load edit AssistanceTicket page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssistanceTicket');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);
      });

      it('edit button click should load edit AssistanceTicket page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssistanceTicket');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);
      });

      it('last delete button click should delete instance of AssistanceTicket', () => {
        cy.intercept('GET', '/api/assistance-tickets/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('assistanceTicket').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assistanceTicketPageUrlPattern);

        assistanceTicket = undefined;
      });
    });
  });

  describe('new AssistanceTicket page', () => {
    beforeEach(() => {
      cy.visit(`${assistanceTicketPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AssistanceTicket');
    });

    it('should create an instance of AssistanceTicket', () => {
      cy.get(`[data-cy="subject"]`).type('abaft');
      cy.get(`[data-cy="subject"]`).should('have.value', 'abaft');

      cy.get(`[data-cy="description"]`).type('over than connect');
      cy.get(`[data-cy="description"]`).should('have.value', 'over than connect');

      cy.get(`[data-cy="status"]`).select('OPEN');

      cy.get(`[data-cy="type"]`).select('OTHER');

      cy.get(`[data-cy="openedAt"]`).type('2025-12-15T15:11');
      cy.get(`[data-cy="openedAt"]`).blur();
      cy.get(`[data-cy="openedAt"]`).should('have.value', '2025-12-15T15:11');

      cy.get(`[data-cy="closedAt"]`).type('2025-12-16T00:46');
      cy.get(`[data-cy="closedAt"]`).blur();
      cy.get(`[data-cy="closedAt"]`).should('have.value', '2025-12-16T00:46');

      cy.get(`[data-cy="comments"]`).type('instead hourly and');
      cy.get(`[data-cy="comments"]`).should('have.value', 'instead hourly and');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T07:33');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T07:33');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T15:52');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T15:52');

      cy.get(`[data-cy="createdBy"]`).type('bobble ouch');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'bobble ouch');

      cy.get(`[data-cy="lastModifiedBy"]`).type('meanwhile');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'meanwhile');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        assistanceTicket = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', assistanceTicketPageUrlPattern);
    });
  });
});
