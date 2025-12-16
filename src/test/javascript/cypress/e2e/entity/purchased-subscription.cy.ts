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
    startDate: '2025-12-15T23:23:16.278Z',
    endDate: '2025-12-15T11:56:45.831Z',
    subscriptionStatus: 'EXPIRED',
    createdDate: '2025-12-15T12:58:02.126Z',
  };

  let purchasedSubscription;
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
        thumbnailS3Key: 'sympathetically oh',
        birthDate: '2025-12-15',
        gender: 'FEMALE',
        createdDate: '2025-12-15T17:41:42.140Z',
        lastModifiedDate: '2025-12-15T19:46:36.272Z',
        createdBy: 'upset',
        lastModifiedBy: 'cool gadzooks save',
        deletedDate: '2025-12-15T13:14:10.122Z',
        nickName: 's_qb52-wr5unvd1',
        fullName: 'D7Yq',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/purchased-subscriptions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/purchased-subscriptions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/purchased-subscriptions/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });

    cy.intercept('GET', '/api/payments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/subscription-plan-offers', {
      statusCode: 200,
      body: [],
    });
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
          body: {
            ...purchasedSubscriptionSample,
            viewer: userLite,
            creator: userLite,
          },
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
      cy.get(`[data-cy="startDate"]`).type('2025-12-16T02:44');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2025-12-16T02:44');

      cy.get(`[data-cy="endDate"]`).type('2025-12-15T11:22');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2025-12-15T11:22');

      cy.get(`[data-cy="subscriptionStatus"]`).select('RENEWED');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T13:36');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T13:36');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T19:41');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T19:41');

      cy.get(`[data-cy="createdBy"]`).type('ack pfft');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'ack pfft');

      cy.get(`[data-cy="lastModifiedBy"]`).type('turbulent excluding hence');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'turbulent excluding hence');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T11:15');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T11:15');

      cy.get(`[data-cy="viewer"]`).select(1);
      cy.get(`[data-cy="creator"]`).select(1);

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
