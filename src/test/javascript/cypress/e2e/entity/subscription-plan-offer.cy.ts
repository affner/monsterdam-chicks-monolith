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

describe('SubscriptionPlanOffer e2e test', () => {
  const subscriptionPlanOfferPageUrl = '/subscription-plan-offer';
  const subscriptionPlanOfferPageUrlPattern = new RegExp('/subscription-plan-offer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subscriptionPlanOfferSample = {
    startDate: '2025-12-16',
    endDate: '2025-12-16',
    promotionType: 'DISCOUNT',
    createdDate: '2025-12-15T19:34:21.547Z',
  };

  let subscriptionPlanOffer;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/subscription-plan-offers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subscription-plan-offers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subscription-plan-offers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subscriptionPlanOffer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subscription-plan-offers/${subscriptionPlanOffer.id}`,
      }).then(() => {
        subscriptionPlanOffer = undefined;
      });
    }
  });

  it('SubscriptionPlanOffers menu should load SubscriptionPlanOffers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subscription-plan-offer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubscriptionPlanOffer').should('exist');
    cy.url().should('match', subscriptionPlanOfferPageUrlPattern);
  });

  describe('SubscriptionPlanOffer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subscriptionPlanOfferPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubscriptionPlanOffer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subscription-plan-offer/new$'));
        cy.getEntityCreateUpdateHeading('SubscriptionPlanOffer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionPlanOfferPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subscription-plan-offers',
          body: subscriptionPlanOfferSample,
        }).then(({ body }) => {
          subscriptionPlanOffer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subscription-plan-offers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/subscription-plan-offers?page=0&size=20>; rel="last",<http://localhost/api/subscription-plan-offers?page=0&size=20>; rel="first"',
              },
              body: [subscriptionPlanOffer],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(subscriptionPlanOfferPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SubscriptionPlanOffer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subscriptionPlanOffer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionPlanOfferPageUrlPattern);
      });

      it('edit button click should load edit SubscriptionPlanOffer page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscriptionPlanOffer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionPlanOfferPageUrlPattern);
      });

      it('edit button click should load edit SubscriptionPlanOffer page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscriptionPlanOffer');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionPlanOfferPageUrlPattern);
      });

      it('last delete button click should delete instance of SubscriptionPlanOffer', () => {
        cy.intercept('GET', '/api/subscription-plan-offers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subscriptionPlanOffer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionPlanOfferPageUrlPattern);

        subscriptionPlanOffer = undefined;
      });
    });
  });

  describe('new SubscriptionPlanOffer page', () => {
    beforeEach(() => {
      cy.visit(`${subscriptionPlanOfferPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubscriptionPlanOffer');
    });

    it('should create an instance of SubscriptionPlanOffer', () => {
      cy.get(`[data-cy="freeDaysDuration"]`).type('PT27M');
      cy.get(`[data-cy="freeDaysDuration"]`).blur();
      cy.get(`[data-cy="freeDaysDuration"]`).should('have.value', 'PT27M');

      cy.get(`[data-cy="discountPercentage"]`).type('66.06');
      cy.get(`[data-cy="discountPercentage"]`).should('have.value', '66.06');

      cy.get(`[data-cy="startDate"]`).type('2025-12-15');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2025-12-15');

      cy.get(`[data-cy="endDate"]`).type('2025-12-15');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2025-12-15');

      cy.get(`[data-cy="subscriptionsLimit"]`).type('3825');
      cy.get(`[data-cy="subscriptionsLimit"]`).should('have.value', '3825');

      cy.get(`[data-cy="promotionType"]`).select('DISCOUNT');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T19:39');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T19:39');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T15:26');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T15:26');

      cy.get(`[data-cy="createdBy"]`).type('councilman');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'councilman');

      cy.get(`[data-cy="lastModifiedBy"]`).type('beneath');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'beneath');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T13:11');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T13:11');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        subscriptionPlanOffer = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', subscriptionPlanOfferPageUrlPattern);
    });
  });
});
