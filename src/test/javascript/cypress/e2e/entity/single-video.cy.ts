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

describe('SingleVideo e2e test', () => {
  const singleVideoPageUrl = '/single-video';
  const singleVideoPageUrlPattern = new RegExp('/single-video(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const singleVideoSample = {
    thumbnail: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    thumbnailContentType: 'unknown',
    thumbnailS3Key: 'eek',
    content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    contentContentType: 'unknown',
    contentS3Key: 'vary',
    isPreview: true,
    createdDate: '2025-12-16T02:30:26.987Z',
  };

  let singleVideo;
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
        thumbnailS3Key: 'circa',
        birthDate: '2025-12-15',
        gender: 'FEMALE',
        createdDate: '2025-12-15T09:29:56.788Z',
        lastModifiedDate: '2025-12-15T18:02:40.635Z',
        createdBy: 'clamp',
        lastModifiedBy: 'lamp',
        deletedDate: '2025-12-15T17:03:09.224Z',
        nickName: '8b6xjc2na-_m',
        fullName: 'nJ9FB',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/single-videos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-videos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-videos/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/content-packages', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
  });

  afterEach(() => {
    if (singleVideo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/single-videos/${singleVideo.id}`,
      }).then(() => {
        singleVideo = undefined;
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

  it('SingleVideos menu should load SingleVideos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('single-video');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SingleVideo').should('exist');
    cy.url().should('match', singleVideoPageUrlPattern);
  });

  describe('SingleVideo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(singleVideoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SingleVideo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/single-video/new$'));
        cy.getEntityCreateUpdateHeading('SingleVideo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/single-videos',
          body: {
            ...singleVideoSample,
            creator: userLite,
          },
        }).then(({ body }) => {
          singleVideo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/single-videos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/single-videos?page=0&size=20>; rel="last",<http://localhost/api/single-videos?page=0&size=20>; rel="first"',
              },
              body: [singleVideo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(singleVideoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SingleVideo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('singleVideo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });

      it('edit button click should load edit SingleVideo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleVideo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });

      it('edit button click should load edit SingleVideo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleVideo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });

      it('last delete button click should delete instance of SingleVideo', () => {
        cy.intercept('GET', '/api/single-videos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('singleVideo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);

        singleVideo = undefined;
      });
    });
  });

  describe('new SingleVideo page', () => {
    beforeEach(() => {
      cy.visit(`${singleVideoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SingleVideo');
    });

    it('should create an instance of SingleVideo', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="thumbnailS3Key"]`).type('strait');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'strait');

      cy.setFieldImageAsBytesOfEntity('content', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="contentS3Key"]`).type('plus');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'plus');

      cy.get(`[data-cy="duration"]`).type('PT4M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT4M');

      cy.get(`[data-cy="likeCount"]`).type('3329');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '3329');

      cy.get(`[data-cy="isPreview"]`).should('not.be.checked');
      cy.get(`[data-cy="isPreview"]`).click();
      cy.get(`[data-cy="isPreview"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-16T01:17');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-16T01:17');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T09:49');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T09:49');

      cy.get(`[data-cy="createdBy"]`).type('ironclad');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'ironclad');

      cy.get(`[data-cy="lastModifiedBy"]`).type('thin');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'thin');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T08:02');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T08:02');

      cy.get(`[data-cy="creator"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        singleVideo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', singleVideoPageUrlPattern);
    });
  });
});
