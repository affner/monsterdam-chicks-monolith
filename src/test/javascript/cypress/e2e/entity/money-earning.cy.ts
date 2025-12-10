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

describe('MoneyEarning e2e test', () => {
  const moneyEarningPageUrl = '/money-earning';
  const moneyEarningPageUrlPattern = new RegExp('/money-earning(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const moneyEarningSample = { amount: 16085.2, createdDate: '2025-12-10T01:29:11.528Z', isDeleted: false, transactionType: 'WITHDRAW' };

  let moneyEarning;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/money-earnings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/money-earnings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/money-earnings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (moneyEarning) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/money-earnings/${moneyEarning.id}`,
      }).then(() => {
        moneyEarning = undefined;
      });
    }
  });

  it('MoneyEarnings menu should load MoneyEarnings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('money-earning');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MoneyEarning').should('exist');
    cy.url().should('match', moneyEarningPageUrlPattern);
  });

  describe('MoneyEarning page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(moneyEarningPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MoneyEarning page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/money-earning/new$'));
        cy.getEntityCreateUpdateHeading('MoneyEarning');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyEarningPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/money-earnings',
          body: moneyEarningSample,
        }).then(({ body }) => {
          moneyEarning = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/money-earnings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/money-earnings?page=0&size=20>; rel="last",<http://localhost/api/money-earnings?page=0&size=20>; rel="first"',
              },
              body: [moneyEarning],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(moneyEarningPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MoneyEarning page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('moneyEarning');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyEarningPageUrlPattern);
      });

      it('edit button click should load edit MoneyEarning page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyEarning');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyEarningPageUrlPattern);
      });

      it('edit button click should load edit MoneyEarning page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyEarning');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyEarningPageUrlPattern);
      });

      it('last delete button click should delete instance of MoneyEarning', () => {
        cy.intercept('GET', '/api/money-earnings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('moneyEarning').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyEarningPageUrlPattern);

        moneyEarning = undefined;
      });
    });
  });

  describe('new MoneyEarning page', () => {
    beforeEach(() => {
      cy.visit(`${moneyEarningPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MoneyEarning');
    });

    it('should create an instance of MoneyEarning', () => {
      cy.get(`[data-cy="amount"]`).type('23659.81');
      cy.get(`[data-cy="amount"]`).should('have.value', '23659.81');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T20:07');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T20:07');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T03:48');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T03:48');

      cy.get(`[data-cy="createdBy"]`).type('blank avalanche outfit');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'blank avalanche outfit');

      cy.get(`[data-cy="lastModifiedBy"]`).type('dependent');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'dependent');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="transactionType"]`).select('WITHDRAW');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        moneyEarning = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', moneyEarningPageUrlPattern);
    });
  });
});
