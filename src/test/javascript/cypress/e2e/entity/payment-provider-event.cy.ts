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

describe('PaymentProviderEvent e2e test', () => {
  const paymentProviderEventPageUrl = '/payment-provider-event';
  const paymentProviderEventPageUrlPattern = new RegExp('/payment-provider-event(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paymentProviderEventSample = {
    providerName: 'hateful spiteful overwork',
    eventType: 'version blah',
    eventId: 'deliberately',
    payloadJson: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    receivedAt: '2025-12-15T21:18:14.377Z',
    processingStatus: 'CANCELED',
  };

  let paymentProviderEvent;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-provider-events+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-provider-events').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-provider-events/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentProviderEvent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-provider-events/${paymentProviderEvent.id}`,
      }).then(() => {
        paymentProviderEvent = undefined;
      });
    }
  });

  it('PaymentProviderEvents menu should load PaymentProviderEvents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-provider-event');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentProviderEvent').should('exist');
    cy.url().should('match', paymentProviderEventPageUrlPattern);
  });

  describe('PaymentProviderEvent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentProviderEventPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentProviderEvent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/payment-provider-event/new$'));
        cy.getEntityCreateUpdateHeading('PaymentProviderEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderEventPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-provider-events',
          body: paymentProviderEventSample,
        }).then(({ body }) => {
          paymentProviderEvent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-provider-events+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payment-provider-events?page=0&size=20>; rel="last",<http://localhost/api/payment-provider-events?page=0&size=20>; rel="first"',
              },
              body: [paymentProviderEvent],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentProviderEventPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentProviderEvent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentProviderEvent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderEventPageUrlPattern);
      });

      it('edit button click should load edit PaymentProviderEvent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentProviderEvent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderEventPageUrlPattern);
      });

      it('edit button click should load edit PaymentProviderEvent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentProviderEvent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderEventPageUrlPattern);
      });

      it('last delete button click should delete instance of PaymentProviderEvent', () => {
        cy.intercept('GET', '/api/payment-provider-events/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('paymentProviderEvent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentProviderEventPageUrlPattern);

        paymentProviderEvent = undefined;
      });
    });
  });

  describe('new PaymentProviderEvent page', () => {
    beforeEach(() => {
      cy.visit(`${paymentProviderEventPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentProviderEvent');
    });

    it('should create an instance of PaymentProviderEvent', () => {
      cy.get(`[data-cy="providerName"]`).type('diagram afford weird');
      cy.get(`[data-cy="providerName"]`).should('have.value', 'diagram afford weird');

      cy.get(`[data-cy="eventType"]`).type('yum oh');
      cy.get(`[data-cy="eventType"]`).should('have.value', 'yum oh');

      cy.get(`[data-cy="eventId"]`).type('fooey');
      cy.get(`[data-cy="eventId"]`).should('have.value', 'fooey');

      cy.get(`[data-cy="payloadJson"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="payloadJson"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="receivedAt"]`).type('2025-12-15T10:06');
      cy.get(`[data-cy="receivedAt"]`).blur();
      cy.get(`[data-cy="receivedAt"]`).should('have.value', '2025-12-15T10:06');

      cy.get(`[data-cy="processedAt"]`).type('2025-12-16T05:38');
      cy.get(`[data-cy="processedAt"]`).blur();
      cy.get(`[data-cy="processedAt"]`).should('have.value', '2025-12-16T05:38');

      cy.get(`[data-cy="processingStatus"]`).select('DECLINED');

      cy.get(`[data-cy="createdBy"]`).type('after honorable once');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'after honorable once');

      cy.get(`[data-cy="lastModifiedBy"]`).type('march');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'march');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T15:11');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T15:11');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        paymentProviderEvent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentProviderEventPageUrlPattern);
    });
  });
});
