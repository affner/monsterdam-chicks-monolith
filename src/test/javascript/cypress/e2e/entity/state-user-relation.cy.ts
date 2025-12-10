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

describe('StateUserRelation e2e test', () => {
  const stateUserRelationPageUrl = '/state-user-relation';
  const stateUserRelationPageUrlPattern = new RegExp('/state-user-relation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const stateUserRelationSample = { createdDate: '2025-12-09T16:03:09.465Z', isDeleted: false };

  let stateUserRelation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/state-user-relations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/state-user-relations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/state-user-relations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (stateUserRelation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/state-user-relations/${stateUserRelation.id}`,
      }).then(() => {
        stateUserRelation = undefined;
      });
    }
  });

  it('StateUserRelations menu should load StateUserRelations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('state-user-relation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StateUserRelation').should('exist');
    cy.url().should('match', stateUserRelationPageUrlPattern);
  });

  describe('StateUserRelation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(stateUserRelationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StateUserRelation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/state-user-relation/new$'));
        cy.getEntityCreateUpdateHeading('StateUserRelation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateUserRelationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/state-user-relations',
          body: stateUserRelationSample,
        }).then(({ body }) => {
          stateUserRelation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/state-user-relations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/state-user-relations?page=0&size=20>; rel="last",<http://localhost/api/state-user-relations?page=0&size=20>; rel="first"',
              },
              body: [stateUserRelation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(stateUserRelationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StateUserRelation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('stateUserRelation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateUserRelationPageUrlPattern);
      });

      it('edit button click should load edit StateUserRelation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StateUserRelation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateUserRelationPageUrlPattern);
      });

      it('edit button click should load edit StateUserRelation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StateUserRelation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateUserRelationPageUrlPattern);
      });

      it('last delete button click should delete instance of StateUserRelation', () => {
        cy.intercept('GET', '/api/state-user-relations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('stateUserRelation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateUserRelationPageUrlPattern);

        stateUserRelation = undefined;
      });
    });
  });

  describe('new StateUserRelation page', () => {
    beforeEach(() => {
      cy.visit(`${stateUserRelationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StateUserRelation');
    });

    it('should create an instance of StateUserRelation', () => {
      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T04:12');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T04:12');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T16:17');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T16:17');

      cy.get(`[data-cy="createdBy"]`).type('weakly');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'weakly');

      cy.get(`[data-cy="lastModifiedBy"]`).type('phooey rule');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'phooey rule');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        stateUserRelation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', stateUserRelationPageUrlPattern);
    });
  });
});
