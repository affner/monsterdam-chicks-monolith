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

describe('BookMark e2e test', () => {
  const bookMarkPageUrl = '/book-mark';
  const bookMarkPageUrlPattern = new RegExp('/book-mark(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const bookMarkSample = { createdDate: '2025-12-09T10:58:49.922Z', isDeleted: true };

  let bookMark;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/book-marks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/book-marks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/book-marks/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (bookMark) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/book-marks/${bookMark.id}`,
      }).then(() => {
        bookMark = undefined;
      });
    }
  });

  it('BookMarks menu should load BookMarks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('book-mark');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BookMark').should('exist');
    cy.url().should('match', bookMarkPageUrlPattern);
  });

  describe('BookMark page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bookMarkPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BookMark page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/book-mark/new$'));
        cy.getEntityCreateUpdateHeading('BookMark');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/book-marks',
          body: bookMarkSample,
        }).then(({ body }) => {
          bookMark = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/book-marks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/book-marks?page=0&size=20>; rel="last",<http://localhost/api/book-marks?page=0&size=20>; rel="first"',
              },
              body: [bookMark],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(bookMarkPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BookMark page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bookMark');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);
      });

      it('edit button click should load edit BookMark page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BookMark');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);
      });

      it('edit button click should load edit BookMark page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BookMark');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);
      });

      it('last delete button click should delete instance of BookMark', () => {
        cy.intercept('GET', '/api/book-marks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('bookMark').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookMarkPageUrlPattern);

        bookMark = undefined;
      });
    });
  });

  describe('new BookMark page', () => {
    beforeEach(() => {
      cy.visit(`${bookMarkPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BookMark');
    });

    it('should create an instance of BookMark', () => {
      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T21:17');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T21:17');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T10:35');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T10:35');

      cy.get(`[data-cy="createdBy"]`).type('um to');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'um to');

      cy.get(`[data-cy="lastModifiedBy"]`).type('abnormally off');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'abnormally off');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        bookMark = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', bookMarkPageUrlPattern);
    });
  });
});
