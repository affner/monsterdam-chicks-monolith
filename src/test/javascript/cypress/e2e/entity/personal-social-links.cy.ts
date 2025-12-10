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

describe('PersonalSocialLinks e2e test', () => {
  const personalSocialLinksPageUrl = '/personal-social-links';
  const personalSocialLinksPageUrlPattern = new RegExp('/personal-social-links(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const personalSocialLinksSample = { socialLink: 'ack', createdDate: '2025-12-10T01:12:45.404Z', isDeleted: false };

  let personalSocialLinks;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/personal-social-links+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/personal-social-links').as('postEntityRequest');
    cy.intercept('DELETE', '/api/personal-social-links/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (personalSocialLinks) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/personal-social-links/${personalSocialLinks.id}`,
      }).then(() => {
        personalSocialLinks = undefined;
      });
    }
  });

  it('PersonalSocialLinks menu should load PersonalSocialLinks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('personal-social-links');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PersonalSocialLinks').should('exist');
    cy.url().should('match', personalSocialLinksPageUrlPattern);
  });

  describe('PersonalSocialLinks page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(personalSocialLinksPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PersonalSocialLinks page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/personal-social-links/new$'));
        cy.getEntityCreateUpdateHeading('PersonalSocialLinks');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/personal-social-links',
          body: personalSocialLinksSample,
        }).then(({ body }) => {
          personalSocialLinks = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/personal-social-links+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/personal-social-links?page=0&size=20>; rel="last",<http://localhost/api/personal-social-links?page=0&size=20>; rel="first"',
              },
              body: [personalSocialLinks],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(personalSocialLinksPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PersonalSocialLinks page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('personalSocialLinks');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);
      });

      it('edit button click should load edit PersonalSocialLinks page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonalSocialLinks');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);
      });

      it('edit button click should load edit PersonalSocialLinks page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonalSocialLinks');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);
      });

      it('last delete button click should delete instance of PersonalSocialLinks', () => {
        cy.intercept('GET', '/api/personal-social-links/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('personalSocialLinks').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personalSocialLinksPageUrlPattern);

        personalSocialLinks = undefined;
      });
    });
  });

  describe('new PersonalSocialLinks page', () => {
    beforeEach(() => {
      cy.visit(`${personalSocialLinksPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PersonalSocialLinks');
    });

    it('should create an instance of PersonalSocialLinks', () => {
      cy.get(`[data-cy="normalImageS3Key"]`).type('overconfidently intently growing');
      cy.get(`[data-cy="normalImageS3Key"]`).should('have.value', 'overconfidently intently growing');

      cy.get(`[data-cy="thumbnailIconS3Key"]`).type('inquisitively black-and-white wee');
      cy.get(`[data-cy="thumbnailIconS3Key"]`).should('have.value', 'inquisitively black-and-white wee');

      cy.get(`[data-cy="socialLink"]`).type('maul pish');
      cy.get(`[data-cy="socialLink"]`).should('have.value', 'maul pish');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T23:21');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T23:21');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T11:50');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T11:50');

      cy.get(`[data-cy="createdBy"]`).type('haircut gah');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'haircut gah');

      cy.get(`[data-cy="lastModifiedBy"]`).type('an hence');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'an hence');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        personalSocialLinks = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', personalSocialLinksPageUrlPattern);
    });
  });
});
