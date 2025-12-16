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

describe('Payment e2e test', () => {
  const paymentPageUrl = '/payment';
  const paymentPageUrlPattern = new RegExp('/payment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paymentSample = {
    amount: 757.66,
    currency: 'fri',
    paymentDate: '2025-12-15T23:59:21.750Z',
    paymentStatus: 'PENDING',
    platformFeeAmount: 6495.87,
    creatorNetAmount: 6322.1,
  };

  let payment;
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
        thumbnailS3Key: 'heavily',
        birthDate: '2025-12-15',
        gender: 'MALE',
        createdDate: '2025-12-15T07:00:53.044Z',
        lastModifiedDate: '2025-12-15T19:44:07.747Z',
        createdBy: 'completion',
        lastModifiedBy: 'hopelessly sharply',
        deletedDate: '2025-12-15T08:15:39.398Z',
        nickName: 'cznel3eupskrzt-',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payments/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/payment-methods', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/payment-providers', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
  });

  afterEach(() => {
    if (payment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payments/${payment.id}`,
      }).then(() => {
        payment = undefined;
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

  it('Payments menu should load Payments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Payment').should('exist');
    cy.url().should('match', paymentPageUrlPattern);
  });

  describe('Payment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Payment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/payment/new$'));
        cy.getEntityCreateUpdateHeading('Payment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payments',
          body: {
            ...paymentSample,
            viewer: userLite,
          },
        }).then(({ body }) => {
          payment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payments?page=0&size=20>; rel="last",<http://localhost/api/payments?page=0&size=20>; rel="first"',
              },
              body: [payment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Payment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('payment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);
      });

      it('edit button click should load edit Payment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Payment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);
      });

      it('edit button click should load edit Payment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Payment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);
      });

      it('last delete button click should delete instance of Payment', () => {
        cy.intercept('GET', '/api/payments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('payment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentPageUrlPattern);

        payment = undefined;
      });
    });
  });

  describe('new Payment page', () => {
    beforeEach(() => {
      cy.visit(`${paymentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Payment');
    });

    it('should create an instance of Payment', () => {
      cy.get(`[data-cy="amount"]`).type('9526.95');
      cy.get(`[data-cy="amount"]`).should('have.value', '9526.95');

      cy.get(`[data-cy="currency"]`).type('phe');
      cy.get(`[data-cy="currency"]`).should('have.value', 'phe');

      cy.get(`[data-cy="paymentDate"]`).type('2025-12-15T22:37');
      cy.get(`[data-cy="paymentDate"]`).blur();
      cy.get(`[data-cy="paymentDate"]`).should('have.value', '2025-12-15T22:37');

      cy.get(`[data-cy="paymentStatus"]`).select('CANCELED');

      cy.get(`[data-cy="paymentReference"]`).type('apostrophize materialise');
      cy.get(`[data-cy="paymentReference"]`).should('have.value', 'apostrophize materialise');

      cy.get(`[data-cy="cloudTransactionId"]`).type('between amidst');
      cy.get(`[data-cy="cloudTransactionId"]`).should('have.value', 'between amidst');

      cy.get(`[data-cy="providerPaymentIntentId"]`).type('mmm querulous fuel');
      cy.get(`[data-cy="providerPaymentIntentId"]`).should('have.value', 'mmm querulous fuel');

      cy.get(`[data-cy="providerChargeId"]`).type('quicker');
      cy.get(`[data-cy="providerChargeId"]`).should('have.value', 'quicker');

      cy.get(`[data-cy="providerCustomerId"]`).type('incidentally');
      cy.get(`[data-cy="providerCustomerId"]`).should('have.value', 'incidentally');

      cy.get(`[data-cy="providerPaymentMethodId"]`).type('whoa');
      cy.get(`[data-cy="providerPaymentMethodId"]`).should('have.value', 'whoa');

      cy.get(`[data-cy="providerEventLastId"]`).type('ick');
      cy.get(`[data-cy="providerEventLastId"]`).should('have.value', 'ick');

      cy.get(`[data-cy="countryCode"]`).type('US');
      cy.get(`[data-cy="countryCode"]`).should('have.value', 'US');

      cy.get(`[data-cy="providerFeeAmount"]`).type('15390.77');
      cy.get(`[data-cy="providerFeeAmount"]`).should('have.value', '15390.77');

      cy.get(`[data-cy="platformFeeAmount"]`).type('7721.77');
      cy.get(`[data-cy="platformFeeAmount"]`).should('have.value', '7721.77');

      cy.get(`[data-cy="creatorNetAmount"]`).type('20669.44');
      cy.get(`[data-cy="creatorNetAmount"]`).should('have.value', '20669.44');

      cy.get(`[data-cy="taxAmount"]`).type('11208.33');
      cy.get(`[data-cy="taxAmount"]`).should('have.value', '11208.33');

      cy.get(`[data-cy="authorizedDate"]`).type('2025-12-15T18:19');
      cy.get(`[data-cy="authorizedDate"]`).blur();
      cy.get(`[data-cy="authorizedDate"]`).should('have.value', '2025-12-15T18:19');

      cy.get(`[data-cy="capturedDate"]`).type('2025-12-15T15:39');
      cy.get(`[data-cy="capturedDate"]`).blur();
      cy.get(`[data-cy="capturedDate"]`).should('have.value', '2025-12-15T15:39');

      cy.get(`[data-cy="viewer"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        payment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentPageUrlPattern);
    });
  });
});
