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

describe('PurchasedSubscription e2e test', () => {
  const purchasedSubscriptionPageUrl = '/purchased-subscription';
  const purchasedSubscriptionPageUrlPattern = new RegExp('/purchased-subscription(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const purchasedSubscriptionSample = {
    startDate: '2025-12-09T23:38:48.578Z',
    endDate: '2025-12-09T20:12:31.676Z',
    subscriptionStatus: 'EXPIRED',
    createdDate: '2025-12-09T15:10:44.811Z',
    isDeleted: true,
    viewerId: 16039,
    creatorId: 17574,
  };

  let purchasedSubscription;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/purchased-subscriptions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/purchased-subscriptions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/purchased-subscriptions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (purchasedSubscription) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/purchased-subscriptions/${purchasedSubscription.id}`,
      }).then(() => {
        purchasedSubscription = undefined;
      });
    }
  });

  it('PurchasedSubscriptions menu should load PurchasedSubscriptions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('purchased-subscription');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PurchasedSubscription').should('exist');
    cy.url().should('match', purchasedSubscriptionPageUrlPattern);
  });

  describe('PurchasedSubscription page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(purchasedSubscriptionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PurchasedSubscription page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/purchased-subscription/new$'));
        cy.getEntityCreateUpdateHeading('PurchasedSubscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/purchased-subscriptions',
          body: purchasedSubscriptionSample,
        }).then(({ body }) => {
          purchasedSubscription = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/purchased-subscriptions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/purchased-subscriptions?page=0&size=20>; rel="last",<http://localhost/api/purchased-subscriptions?page=0&size=20>; rel="first"',
              },
              body: [purchasedSubscription],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(purchasedSubscriptionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PurchasedSubscription page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('purchasedSubscription');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);
      });

      it('edit button click should load edit PurchasedSubscription page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedSubscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);
      });

      it('edit button click should load edit PurchasedSubscription page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedSubscription');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);
      });

      it('last delete button click should delete instance of PurchasedSubscription', () => {
        cy.intercept('GET', '/api/purchased-subscriptions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('purchasedSubscription').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedSubscriptionPageUrlPattern);

        purchasedSubscription = undefined;
      });
    });
  });

  describe('new PurchasedSubscription page', () => {
    beforeEach(() => {
      cy.visit(`${purchasedSubscriptionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PurchasedSubscription');
    });

    it('should create an instance of PurchasedSubscription', () => {
      cy.get(`[data-cy="startDate"]`).type('2025-12-09T14:58');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2025-12-09T14:58');

      cy.get(`[data-cy="endDate"]`).type('2025-12-09T16:59');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2025-12-09T16:59');

      cy.get(`[data-cy="subscriptionStatus"]`).select('EXPIRED');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T00:15');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T00:15');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T08:36');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T08:36');

      cy.get(`[data-cy="createdBy"]`).type('subexpression');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'subexpression');

      cy.get(`[data-cy="lastModifiedBy"]`).type('fireplace tenderly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'fireplace tenderly');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="viewerId"]`).type('31566');
      cy.get(`[data-cy="viewerId"]`).should('have.value', '31566');

      cy.get(`[data-cy="creatorId"]`).type('17803');
      cy.get(`[data-cy="creatorId"]`).should('have.value', '17803');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        purchasedSubscription = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', purchasedSubscriptionPageUrlPattern);
    });
  });
});
