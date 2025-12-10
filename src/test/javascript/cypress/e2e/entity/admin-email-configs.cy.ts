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

describe('AdminEmailConfigs e2e test', () => {
  const adminEmailConfigsPageUrl = '/admin-email-configs';
  const adminEmailConfigsPageUrlPattern = new RegExp('/admin-email-configs(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const adminEmailConfigsSample = {
    title: 'beyond',
    subject: 'privilege instead mid',
    content: 'privilege athwart',
    mailTemplateType: 'NEWSLETTER',
    createdDate: '2025-12-10T01:41:32.727Z',
    isActive: false,
  };

  let adminEmailConfigs;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/admin-email-configs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/admin-email-configs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/admin-email-configs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (adminEmailConfigs) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/admin-email-configs/${adminEmailConfigs.id}`,
      }).then(() => {
        adminEmailConfigs = undefined;
      });
    }
  });

  it('AdminEmailConfigs menu should load AdminEmailConfigs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('admin-email-configs');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AdminEmailConfigs').should('exist');
    cy.url().should('match', adminEmailConfigsPageUrlPattern);
  });

  describe('AdminEmailConfigs page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(adminEmailConfigsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AdminEmailConfigs page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/admin-email-configs/new$'));
        cy.getEntityCreateUpdateHeading('AdminEmailConfigs');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminEmailConfigsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/admin-email-configs',
          body: adminEmailConfigsSample,
        }).then(({ body }) => {
          adminEmailConfigs = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/admin-email-configs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/admin-email-configs?page=0&size=20>; rel="last",<http://localhost/api/admin-email-configs?page=0&size=20>; rel="first"',
              },
              body: [adminEmailConfigs],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(adminEmailConfigsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AdminEmailConfigs page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('adminEmailConfigs');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminEmailConfigsPageUrlPattern);
      });

      it('edit button click should load edit AdminEmailConfigs page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AdminEmailConfigs');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminEmailConfigsPageUrlPattern);
      });

      it('edit button click should load edit AdminEmailConfigs page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AdminEmailConfigs');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminEmailConfigsPageUrlPattern);
      });

      it('last delete button click should delete instance of AdminEmailConfigs', () => {
        cy.intercept('GET', '/api/admin-email-configs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('adminEmailConfigs').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminEmailConfigsPageUrlPattern);

        adminEmailConfigs = undefined;
      });
    });
  });

  describe('new AdminEmailConfigs page', () => {
    beforeEach(() => {
      cy.visit(`${adminEmailConfigsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AdminEmailConfigs');
    });

    it('should create an instance of AdminEmailConfigs', () => {
      cy.get(`[data-cy="title"]`).type('endow really');
      cy.get(`[data-cy="title"]`).should('have.value', 'endow really');

      cy.get(`[data-cy="subject"]`).type('astride um ceramics');
      cy.get(`[data-cy="subject"]`).should('have.value', 'astride um ceramics');

      cy.get(`[data-cy="content"]`).type('wolf');
      cy.get(`[data-cy="content"]`).should('have.value', 'wolf');

      cy.get(`[data-cy="mailTemplateType"]`).select('NEWSLETTER');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T06:55');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T06:55');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T10:28');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T10:28');

      cy.get(`[data-cy="createdBy"]`).type('since sour');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'since sour');

      cy.get(`[data-cy="lastModifiedBy"]`).type('yowza');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'yowza');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        adminEmailConfigs = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', adminEmailConfigsPageUrlPattern);
    });
  });
});
