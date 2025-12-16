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

describe('State e2e test', () => {
  const statePageUrl = '/state';
  const statePageUrlPattern = new RegExp('/state(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const stateSample = { stateName: 'unique', isoCode: 'how', createdDate: '2025-12-16T01:59:20.385Z' };

  let state;
  let country;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/countries',
      body: {
        name: 'unlawful glum',
        alpha2Code: 'bo',
        alpha3Code: 'ast',
        phoneCode: 'rewrite on',
        thumbnailCountry: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        thumbnailCountryContentType: 'unknown',
        createdDate: '2025-12-15T13:27:55.713Z',
        lastModifiedDate: '2025-12-15T19:42:21.419Z',
        createdBy: 'beneath',
        lastModifiedBy: 'especially huzzah',
        deletedDate: '2025-12-15T22:00:17.127Z',
      },
    }).then(({ body }) => {
      country = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/states+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/states').as('postEntityRequest');
    cy.intercept('DELETE', '/api/states/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/countries', {
      statusCode: 200,
      body: [country],
    });
  });

  afterEach(() => {
    if (state) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/states/${state.id}`,
      }).then(() => {
        state = undefined;
      });
    }
  });

  afterEach(() => {
    if (country) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/countries/${country.id}`,
      }).then(() => {
        country = undefined;
      });
    }
  });

  it('States menu should load States page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('state');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('State').should('exist');
    cy.url().should('match', statePageUrlPattern);
  });

  describe('State page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(statePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create State page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/state/new$'));
        cy.getEntityCreateUpdateHeading('State');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/states',
          body: {
            ...stateSample,
            country,
          },
        }).then(({ body }) => {
          state = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/states+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/states?page=0&size=20>; rel="last",<http://localhost/api/states?page=0&size=20>; rel="first"',
              },
              body: [state],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(statePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details State page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('state');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });

      it('edit button click should load edit State page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('State');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });

      it('edit button click should load edit State page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('State');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);
      });

      it('last delete button click should delete instance of State', () => {
        cy.intercept('GET', '/api/states/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('state').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', statePageUrlPattern);

        state = undefined;
      });
    });
  });

  describe('new State page', () => {
    beforeEach(() => {
      cy.visit(`${statePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('State');
    });

    it('should create an instance of State', () => {
      cy.get(`[data-cy="stateName"]`).type('male fooey spherical');
      cy.get(`[data-cy="stateName"]`).should('have.value', 'male fooey spherical');

      cy.get(`[data-cy="isoCode"]`).type('ann');
      cy.get(`[data-cy="isoCode"]`).should('have.value', 'ann');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T19:14');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T19:14');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T16:15');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T16:15');

      cy.get(`[data-cy="createdBy"]`).type('aw');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'aw');

      cy.get(`[data-cy="lastModifiedBy"]`).type('blah via');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'blah via');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-16T03:03');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-16T03:03');

      cy.get(`[data-cy="country"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        state = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', statePageUrlPattern);
    });
  });
});
