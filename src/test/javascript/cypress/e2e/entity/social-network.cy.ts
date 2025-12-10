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

describe('SocialNetwork e2e test', () => {
  const socialNetworkPageUrl = '/social-network';
  const socialNetworkPageUrlPattern = new RegExp('/social-network(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const socialNetworkSample = {
    name: 'boldly',
    completeName: 'bashfully weight resort',
    mainLink: 'now mmm within',
    createdDate: '2025-12-09T16:16:57.992Z',
    isDeleted: true,
  };

  let socialNetwork;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/social-networks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/social-networks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/social-networks/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (socialNetwork) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/social-networks/${socialNetwork.id}`,
      }).then(() => {
        socialNetwork = undefined;
      });
    }
  });

  it('SocialNetworks menu should load SocialNetworks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('social-network');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SocialNetwork').should('exist');
    cy.url().should('match', socialNetworkPageUrlPattern);
  });

  describe('SocialNetwork page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(socialNetworkPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SocialNetwork page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/social-network/new$'));
        cy.getEntityCreateUpdateHeading('SocialNetwork');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', socialNetworkPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/social-networks',
          body: socialNetworkSample,
        }).then(({ body }) => {
          socialNetwork = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/social-networks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/social-networks?page=0&size=20>; rel="last",<http://localhost/api/social-networks?page=0&size=20>; rel="first"',
              },
              body: [socialNetwork],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(socialNetworkPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SocialNetwork page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('socialNetwork');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', socialNetworkPageUrlPattern);
      });

      it('edit button click should load edit SocialNetwork page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialNetwork');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', socialNetworkPageUrlPattern);
      });

      it('edit button click should load edit SocialNetwork page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialNetwork');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', socialNetworkPageUrlPattern);
      });

      it('last delete button click should delete instance of SocialNetwork', () => {
        cy.intercept('GET', '/api/social-networks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('socialNetwork').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', socialNetworkPageUrlPattern);

        socialNetwork = undefined;
      });
    });
  });

  describe('new SocialNetwork page', () => {
    beforeEach(() => {
      cy.visit(`${socialNetworkPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SocialNetwork');
    });

    it('should create an instance of SocialNetwork', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="name"]`).type('an');
      cy.get(`[data-cy="name"]`).should('have.value', 'an');

      cy.get(`[data-cy="completeName"]`).type('hence out inasmuch');
      cy.get(`[data-cy="completeName"]`).should('have.value', 'hence out inasmuch');

      cy.get(`[data-cy="mainLink"]`).type('untried');
      cy.get(`[data-cy="mainLink"]`).should('have.value', 'untried');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T16:23');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T16:23');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T06:22');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T06:22');

      cy.get(`[data-cy="createdBy"]`).type('strategy ah');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'strategy ah');

      cy.get(`[data-cy="lastModifiedBy"]`).type('reasoning');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'reasoning');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        socialNetwork = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', socialNetworkPageUrlPattern);
    });
  });
});
