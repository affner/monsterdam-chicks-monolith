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

describe('TaxInfo e2e test', () => {
  const taxInfoPageUrl = '/tax-info';
  const taxInfoPageUrlPattern = new RegExp('/tax-info(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const taxInfoSample = { taxType: 'VAT', createdDate: '2025-12-09T22:06:14.230Z', isDeleted: false };

  let taxInfo;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tax-infos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tax-infos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tax-infos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (taxInfo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-infos/${taxInfo.id}`,
      }).then(() => {
        taxInfo = undefined;
      });
    }
  });

  it('TaxInfos menu should load TaxInfos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tax-info');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaxInfo').should('exist');
    cy.url().should('match', taxInfoPageUrlPattern);
  });

  describe('TaxInfo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taxInfoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaxInfo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tax-info/new$'));
        cy.getEntityCreateUpdateHeading('TaxInfo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tax-infos',
          body: taxInfoSample,
        }).then(({ body }) => {
          taxInfo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tax-infos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tax-infos?page=0&size=20>; rel="last",<http://localhost/api/tax-infos?page=0&size=20>; rel="first"',
              },
              body: [taxInfo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(taxInfoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaxInfo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taxInfo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);
      });

      it('edit button click should load edit TaxInfo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxInfo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);
      });

      it('edit button click should load edit TaxInfo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxInfo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);
      });

      it('last delete button click should delete instance of TaxInfo', () => {
        cy.intercept('GET', '/api/tax-infos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('taxInfo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxInfoPageUrlPattern);

        taxInfo = undefined;
      });
    });
  });

  describe('new TaxInfo page', () => {
    beforeEach(() => {
      cy.visit(`${taxInfoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TaxInfo');
    });

    it('should create an instance of TaxInfo', () => {
      cy.get(`[data-cy="ratePercentage"]`).type('15185.41');
      cy.get(`[data-cy="ratePercentage"]`).should('have.value', '15185.41');

      cy.get(`[data-cy="taxType"]`).select('WITHHOLDING');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T08:09');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T08:09');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T08:44');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T08:44');

      cy.get(`[data-cy="createdBy"]`).type('proud um notwithstanding');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'proud um notwithstanding');

      cy.get(`[data-cy="lastModifiedBy"]`).type('tail');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'tail');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        taxInfo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', taxInfoPageUrlPattern);
    });
  });
});
