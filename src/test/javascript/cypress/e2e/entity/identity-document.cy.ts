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

describe('IdentityDocument e2e test', () => {
  const identityDocumentPageUrl = '/identity-document';
  const identityDocumentPageUrlPattern = new RegExp('/identity-document(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const identityDocumentSample = {
    documentName: 'afore vibration',
    fileDocument: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    fileDocumentContentType: 'unknown',
    fileDocumentS3Key: 'sunbeam hassle morbidity',
    createdDate: '2025-12-15T11:42:35.523Z',
  };

  let identityDocument;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/identity-documents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/identity-documents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/identity-documents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (identityDocument) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/identity-documents/${identityDocument.id}`,
      }).then(() => {
        identityDocument = undefined;
      });
    }
  });

  it('IdentityDocuments menu should load IdentityDocuments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('identity-document');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IdentityDocument').should('exist');
    cy.url().should('match', identityDocumentPageUrlPattern);
  });

  describe('IdentityDocument page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(identityDocumentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IdentityDocument page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/identity-document/new$'));
        cy.getEntityCreateUpdateHeading('IdentityDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/identity-documents',
          body: identityDocumentSample,
        }).then(({ body }) => {
          identityDocument = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/identity-documents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/identity-documents?page=0&size=20>; rel="last",<http://localhost/api/identity-documents?page=0&size=20>; rel="first"',
              },
              body: [identityDocument],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(identityDocumentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IdentityDocument page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('identityDocument');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);
      });

      it('edit button click should load edit IdentityDocument page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);
      });

      it('edit button click should load edit IdentityDocument page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocument');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);
      });

      it('last delete button click should delete instance of IdentityDocument', () => {
        cy.intercept('GET', '/api/identity-documents/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('identityDocument').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentPageUrlPattern);

        identityDocument = undefined;
      });
    });
  });

  describe('new IdentityDocument page', () => {
    beforeEach(() => {
      cy.visit(`${identityDocumentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IdentityDocument');
    });

    it('should create an instance of IdentityDocument', () => {
      cy.get(`[data-cy="documentName"]`).type('wrong as');
      cy.get(`[data-cy="documentName"]`).should('have.value', 'wrong as');

      cy.get(`[data-cy="documentDescription"]`).type('amidst lest');
      cy.get(`[data-cy="documentDescription"]`).should('have.value', 'amidst lest');

      cy.get(`[data-cy="documentStatus"]`).select('PENDING');

      cy.get(`[data-cy="documentType"]`).select('CONTRACT');

      cy.setFieldImageAsBytesOfEntity('fileDocument', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="fileDocumentS3Key"]`).type('wherever');
      cy.get(`[data-cy="fileDocumentS3Key"]`).should('have.value', 'wherever');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T11:02');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T11:02');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T09:32');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T09:32');

      cy.get(`[data-cy="createdBy"]`).type('great incomplete');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'great incomplete');

      cy.get(`[data-cy="lastModifiedBy"]`).type('wherever swing');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'wherever swing');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        identityDocument = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', identityDocumentPageUrlPattern);
    });
  });
});
