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

describe('UserAssociation e2e test', () => {
  const userAssociationPageUrl = '/user-association';
  const userAssociationPageUrlPattern = new RegExp('/user-association(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userAssociationSample = {
    requestedDate: '2025-12-09T11:40:48.011Z',
    associationToken: 'frenetically heavily equate',
    expiryDate: '2025-12-10T03:09:20.938Z',
    createdDate: '2025-12-10T09:03:38.699Z',
    isDeleted: true,
    ownerId: 13253,
  };

  let userAssociation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-associations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-associations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-associations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userAssociation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-associations/${userAssociation.id}`,
      }).then(() => {
        userAssociation = undefined;
      });
    }
  });

  it('UserAssociations menu should load UserAssociations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-association');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserAssociation').should('exist');
    cy.url().should('match', userAssociationPageUrlPattern);
  });

  describe('UserAssociation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userAssociationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserAssociation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-association/new$'));
        cy.getEntityCreateUpdateHeading('UserAssociation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-associations',
          body: userAssociationSample,
        }).then(({ body }) => {
          userAssociation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-associations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-associations?page=0&size=20>; rel="last",<http://localhost/api/user-associations?page=0&size=20>; rel="first"',
              },
              body: [userAssociation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userAssociationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserAssociation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userAssociation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);
      });

      it('edit button click should load edit UserAssociation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAssociation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);
      });

      it('edit button click should load edit UserAssociation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserAssociation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);
      });

      it('last delete button click should delete instance of UserAssociation', () => {
        cy.intercept('GET', '/api/user-associations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userAssociation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userAssociationPageUrlPattern);

        userAssociation = undefined;
      });
    });
  });

  describe('new UserAssociation page', () => {
    beforeEach(() => {
      cy.visit(`${userAssociationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserAssociation');
    });

    it('should create an instance of UserAssociation', () => {
      cy.get(`[data-cy="requestedDate"]`).type('2025-12-10T05:29');
      cy.get(`[data-cy="requestedDate"]`).blur();
      cy.get(`[data-cy="requestedDate"]`).should('have.value', '2025-12-10T05:29');

      cy.get(`[data-cy="status"]`).select('REJECTED');

      cy.get(`[data-cy="associationToken"]`).type('underneath innovate where');
      cy.get(`[data-cy="associationToken"]`).should('have.value', 'underneath innovate where');

      cy.get(`[data-cy="expiryDate"]`).type('2025-12-09T14:10');
      cy.get(`[data-cy="expiryDate"]`).blur();
      cy.get(`[data-cy="expiryDate"]`).should('have.value', '2025-12-09T14:10');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T01:29');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T01:29');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T21:45');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T21:45');

      cy.get(`[data-cy="createdBy"]`).type('acidly');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'acidly');

      cy.get(`[data-cy="lastModifiedBy"]`).type('clone well-groomed truly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'clone well-groomed truly');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="ownerId"]`).type('26128');
      cy.get(`[data-cy="ownerId"]`).should('have.value', '26128');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userAssociation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userAssociationPageUrlPattern);
    });
  });
});
