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

describe('SubscriptionBundle e2e test', () => {
  const subscriptionBundlePageUrl = '/subscription-bundle';
  const subscriptionBundlePageUrlPattern = new RegExp('/subscription-bundle(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subscriptionBundleSample = { amount: 508.71, duration: 14612, createdDate: '2025-12-09T18:37:03.375Z', isDeleted: true };

  let subscriptionBundle;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/subscription-bundles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subscription-bundles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subscription-bundles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subscriptionBundle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subscription-bundles/${subscriptionBundle.id}`,
      }).then(() => {
        subscriptionBundle = undefined;
      });
    }
  });

  it('SubscriptionBundles menu should load SubscriptionBundles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subscription-bundle');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubscriptionBundle').should('exist');
    cy.url().should('match', subscriptionBundlePageUrlPattern);
  });

  describe('SubscriptionBundle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subscriptionBundlePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubscriptionBundle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subscription-bundle/new$'));
        cy.getEntityCreateUpdateHeading('SubscriptionBundle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subscription-bundles',
          body: subscriptionBundleSample,
        }).then(({ body }) => {
          subscriptionBundle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subscription-bundles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/subscription-bundles?page=0&size=20>; rel="last",<http://localhost/api/subscription-bundles?page=0&size=20>; rel="first"',
              },
              body: [subscriptionBundle],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(subscriptionBundlePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SubscriptionBundle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subscriptionBundle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);
      });

      it('edit button click should load edit SubscriptionBundle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscriptionBundle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);
      });

      it('edit button click should load edit SubscriptionBundle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubscriptionBundle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);
      });

      it('last delete button click should delete instance of SubscriptionBundle', () => {
        cy.intercept('GET', '/api/subscription-bundles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subscriptionBundle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subscriptionBundlePageUrlPattern);

        subscriptionBundle = undefined;
      });
    });
  });

  describe('new SubscriptionBundle page', () => {
    beforeEach(() => {
      cy.visit(`${subscriptionBundlePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubscriptionBundle');
    });

    it('should create an instance of SubscriptionBundle', () => {
      cy.get(`[data-cy="amount"]`).type('117.67');
      cy.get(`[data-cy="amount"]`).should('have.value', '117.67');

      cy.get(`[data-cy="duration"]`).type('PT22M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT22M');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T14:33');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T14:33');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T09:58');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T09:58');

      cy.get(`[data-cy="createdBy"]`).type('yet');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'yet');

      cy.get(`[data-cy="lastModifiedBy"]`).type('jittery phooey before');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'jittery phooey before');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        subscriptionBundle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', subscriptionBundlePageUrlPattern);
    });
  });
});
