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
    requestedDate: '2025-12-16T05:24:51.359Z',
    associationToken: 'sup geez next',
    expiryDate: '2025-12-16T01:34:08.888Z',
    createdDate: '2025-12-15T13:29:33.755Z',
  };

  let userAssociation;
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
        thumbnailS3Key: 'negotiation absentmindedly busily',
        birthDate: '2025-12-15',
        gender: 'MALE',
        createdDate: '2025-12-15T11:35:46.594Z',
        lastModifiedDate: '2025-12-15T21:59:28.736Z',
        createdBy: 'extremely',
        lastModifiedBy: 'duh yum',
        deletedDate: '2025-12-15T19:03:22.157Z',
        nickName: 'wc_1mtfb',
        fullName: 'eV',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-associations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-associations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-associations/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
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
          body: {
            ...userAssociationSample,
            owner: userLite,
          },
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
      cy.get(`[data-cy="requestedDate"]`).type('2025-12-15T09:00');
      cy.get(`[data-cy="requestedDate"]`).blur();
      cy.get(`[data-cy="requestedDate"]`).should('have.value', '2025-12-15T09:00');

      cy.get(`[data-cy="status"]`).select('REJECTED');

      cy.get(`[data-cy="associationToken"]`).type('inasmuch priesthood gleefully');
      cy.get(`[data-cy="associationToken"]`).should('have.value', 'inasmuch priesthood gleefully');

      cy.get(`[data-cy="expiryDate"]`).type('2025-12-16T01:11');
      cy.get(`[data-cy="expiryDate"]`).blur();
      cy.get(`[data-cy="expiryDate"]`).should('have.value', '2025-12-16T01:11');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T10:48');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T10:48');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T22:08');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T22:08');

      cy.get(`[data-cy="createdBy"]`).type('beside whose');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'beside whose');

      cy.get(`[data-cy="lastModifiedBy"]`).type('phew co-producer');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'phew co-producer');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-16T02:23');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-16T02:23');

      cy.get(`[data-cy="owner"]`).select(1);

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
