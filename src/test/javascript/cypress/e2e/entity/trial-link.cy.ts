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

describe('TrialLink e2e test', () => {
  const trialLinkPageUrl = '/trial-link';
  const trialLinkPageUrlPattern = new RegExp('/trial-link(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const trialLinkSample = {
    linkCode: 'aha',
    startDate: '2025-12-09',
    endDate: '2025-12-09',
    isUsed: false,
    createdDate: '2025-12-09T13:42:21.534Z',
    isDeleted: true,
  };

  let trialLink;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/trial-links+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/trial-links').as('postEntityRequest');
    cy.intercept('DELETE', '/api/trial-links/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (trialLink) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/trial-links/${trialLink.id}`,
      }).then(() => {
        trialLink = undefined;
      });
    }
  });

  it('TrialLinks menu should load TrialLinks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('trial-link');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TrialLink').should('exist');
    cy.url().should('match', trialLinkPageUrlPattern);
  });

  describe('TrialLink page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(trialLinkPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TrialLink page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/trial-link/new$'));
        cy.getEntityCreateUpdateHeading('TrialLink');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trialLinkPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/trial-links',
          body: trialLinkSample,
        }).then(({ body }) => {
          trialLink = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/trial-links+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/trial-links?page=0&size=20>; rel="last",<http://localhost/api/trial-links?page=0&size=20>; rel="first"',
              },
              body: [trialLink],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(trialLinkPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TrialLink page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('trialLink');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trialLinkPageUrlPattern);
      });

      it('edit button click should load edit TrialLink page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TrialLink');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trialLinkPageUrlPattern);
      });

      it('edit button click should load edit TrialLink page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TrialLink');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trialLinkPageUrlPattern);
      });

      it('last delete button click should delete instance of TrialLink', () => {
        cy.intercept('GET', '/api/trial-links/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('trialLink').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trialLinkPageUrlPattern);

        trialLink = undefined;
      });
    });
  });

  describe('new TrialLink page', () => {
    beforeEach(() => {
      cy.visit(`${trialLinkPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TrialLink');
    });

    it('should create an instance of TrialLink', () => {
      cy.get(`[data-cy="linkCode"]`).type('shark tenderly orient');
      cy.get(`[data-cy="linkCode"]`).should('have.value', 'shark tenderly orient');

      cy.get(`[data-cy="startDate"]`).type('2025-12-10');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2025-12-10');

      cy.get(`[data-cy="endDate"]`).type('2025-12-09');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2025-12-09');

      cy.get(`[data-cy="freeDays"]`).type('11750');
      cy.get(`[data-cy="freeDays"]`).should('have.value', '11750');

      cy.get(`[data-cy="isUsed"]`).should('not.be.checked');
      cy.get(`[data-cy="isUsed"]`).click();
      cy.get(`[data-cy="isUsed"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T15:48');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T15:48');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T22:20');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T22:20');

      cy.get(`[data-cy="createdBy"]`).type('mad release');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'mad release');

      cy.get(`[data-cy="lastModifiedBy"]`).type('officially');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'officially');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        trialLink = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', trialLinkPageUrlPattern);
    });
  });
});
