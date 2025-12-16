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

describe('MoneyGift e2e test', () => {
  const moneyGiftPageUrl = '/money-gift';
  const moneyGiftPageUrlPattern = new RegExp('/money-gift(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const moneyGiftSample = { amount: 10386.93, currency: 'int', createdDate: '2025-12-15T09:00:10.192Z' };

  let moneyGift;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/money-gifts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/money-gifts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/money-gifts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (moneyGift) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/money-gifts/${moneyGift.id}`,
      }).then(() => {
        moneyGift = undefined;
      });
    }
  });

  it('MoneyGifts menu should load MoneyGifts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('money-gift');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MoneyGift').should('exist');
    cy.url().should('match', moneyGiftPageUrlPattern);
  });

  describe('MoneyGift page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(moneyGiftPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MoneyGift page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/money-gift/new$'));
        cy.getEntityCreateUpdateHeading('MoneyGift');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyGiftPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/money-gifts',
          body: moneyGiftSample,
        }).then(({ body }) => {
          moneyGift = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/money-gifts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/money-gifts?page=0&size=20>; rel="last",<http://localhost/api/money-gifts?page=0&size=20>; rel="first"',
              },
              body: [moneyGift],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(moneyGiftPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MoneyGift page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('moneyGift');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyGiftPageUrlPattern);
      });

      it('edit button click should load edit MoneyGift page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyGift');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyGiftPageUrlPattern);
      });

      it('edit button click should load edit MoneyGift page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyGift');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyGiftPageUrlPattern);
      });

      it('last delete button click should delete instance of MoneyGift', () => {
        cy.intercept('GET', '/api/money-gifts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('moneyGift').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyGiftPageUrlPattern);

        moneyGift = undefined;
      });
    });
  });

  describe('new MoneyGift page', () => {
    beforeEach(() => {
      cy.visit(`${moneyGiftPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MoneyGift');
    });

    it('should create an instance of MoneyGift', () => {
      cy.get(`[data-cy="amount"]`).type('12427.57');
      cy.get(`[data-cy="amount"]`).should('have.value', '12427.57');

      cy.get(`[data-cy="currency"]`).type('hou');
      cy.get(`[data-cy="currency"]`).should('have.value', 'hou');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T08:40');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T08:40');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T19:44');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T19:44');

      cy.get(`[data-cy="createdBy"]`).type('brr');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'brr');

      cy.get(`[data-cy="lastModifiedBy"]`).type('far-off jealous frankly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'far-off jealous frankly');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T07:08');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T07:08');

      cy.get(`[data-cy="messageId"]`).type('12165');
      cy.get(`[data-cy="messageId"]`).should('have.value', '12165');

      cy.get(`[data-cy="postId"]`).type('27575');
      cy.get(`[data-cy="postId"]`).should('have.value', '27575');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        moneyGift = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', moneyGiftPageUrlPattern);
    });
  });
});
