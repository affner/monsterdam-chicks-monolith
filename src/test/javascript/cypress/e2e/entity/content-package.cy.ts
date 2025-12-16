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

describe('ContentPackage e2e test', () => {
  const contentPackagePageUrl = '/content-package';
  const contentPackagePageUrlPattern = new RegExp('/content-package(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const contentPackageSample = { isPaidContent: true, createdDate: '2025-12-16T00:03:42.970Z' };

  let contentPackage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/content-packages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/content-packages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/content-packages/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contentPackage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/content-packages/${contentPackage.id}`,
      }).then(() => {
        contentPackage = undefined;
      });
    }
  });

  it('ContentPackages menu should load ContentPackages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('content-package');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContentPackage').should('exist');
    cy.url().should('match', contentPackagePageUrlPattern);
  });

  describe('ContentPackage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contentPackagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContentPackage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/content-package/new$'));
        cy.getEntityCreateUpdateHeading('ContentPackage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/content-packages',
          body: contentPackageSample,
        }).then(({ body }) => {
          contentPackage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/content-packages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/content-packages?page=0&size=20>; rel="last",<http://localhost/api/content-packages?page=0&size=20>; rel="first"',
              },
              body: [contentPackage],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contentPackagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContentPackage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contentPackage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);
      });

      it('edit button click should load edit ContentPackage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContentPackage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);
      });

      it('edit button click should load edit ContentPackage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContentPackage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);
      });

      it('last delete button click should delete instance of ContentPackage', () => {
        cy.intercept('GET', '/api/content-packages/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('contentPackage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contentPackagePageUrlPattern);

        contentPackage = undefined;
      });
    });
  });

  describe('new ContentPackage page', () => {
    beforeEach(() => {
      cy.visit(`${contentPackagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContentPackage');
    });

    it('should create an instance of ContentPackage', () => {
      cy.get(`[data-cy="amount"]`).type('6599.13');
      cy.get(`[data-cy="amount"]`).should('have.value', '6599.13');

      cy.get(`[data-cy="videoCount"]`).type('22416');
      cy.get(`[data-cy="videoCount"]`).should('have.value', '22416');

      cy.get(`[data-cy="imageCount"]`).type('2931');
      cy.get(`[data-cy="imageCount"]`).should('have.value', '2931');

      cy.get(`[data-cy="isPaidContent"]`).should('not.be.checked');
      cy.get(`[data-cy="isPaidContent"]`).click();
      cy.get(`[data-cy="isPaidContent"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T21:10');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T21:10');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T21:08');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T21:08');

      cy.get(`[data-cy="createdBy"]`).type('blowgun');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'blowgun');

      cy.get(`[data-cy="lastModifiedBy"]`).type('posh huzzah weep');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'posh huzzah weep');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T21:43');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T21:43');

      cy.get(`[data-cy="messageId"]`).type('22832');
      cy.get(`[data-cy="messageId"]`).should('have.value', '22832');

      cy.get(`[data-cy="postId"]`).type('13043');
      cy.get(`[data-cy="postId"]`).should('have.value', '13043');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contentPackage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', contentPackagePageUrlPattern);
    });
  });
});
