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

describe('VideoStory e2e test', () => {
  const videoStoryPageUrl = '/video-story';
  const videoStoryPageUrlPattern = new RegExp('/video-story(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const videoStorySample = {
    thumbnailS3Key: 'once unaccountably afore',
    contentS3Key: 'deafening out',
    createdDate: '2025-12-10T01:12:56.108Z',
    isDeleted: true,
    creatorId: 1010,
  };

  let videoStory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/video-stories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/video-stories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/video-stories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (videoStory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/video-stories/${videoStory.id}`,
      }).then(() => {
        videoStory = undefined;
      });
    }
  });

  it('VideoStories menu should load VideoStories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('video-story');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VideoStory').should('exist');
    cy.url().should('match', videoStoryPageUrlPattern);
  });

  describe('VideoStory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(videoStoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VideoStory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/video-story/new$'));
        cy.getEntityCreateUpdateHeading('VideoStory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/video-stories',
          body: videoStorySample,
        }).then(({ body }) => {
          videoStory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/video-stories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/video-stories?page=0&size=20>; rel="last",<http://localhost/api/video-stories?page=0&size=20>; rel="first"',
              },
              body: [videoStory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(videoStoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VideoStory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('videoStory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);
      });

      it('edit button click should load edit VideoStory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VideoStory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);
      });

      it('edit button click should load edit VideoStory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VideoStory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);
      });

      it('last delete button click should delete instance of VideoStory', () => {
        cy.intercept('GET', '/api/video-stories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('videoStory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', videoStoryPageUrlPattern);

        videoStory = undefined;
      });
    });
  });

  describe('new VideoStory page', () => {
    beforeEach(() => {
      cy.visit(`${videoStoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VideoStory');
    });

    it('should create an instance of VideoStory', () => {
      cy.get(`[data-cy="thumbnailS3Key"]`).type('everlasting wrongly');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'everlasting wrongly');

      cy.get(`[data-cy="contentS3Key"]`).type('claw');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'claw');

      cy.get(`[data-cy="duration"]`).type('PT56M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT56M');

      cy.get(`[data-cy="likeCount"]`).type('3121');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '3121');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T05:35');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T05:35');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T00:44');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T00:44');

      cy.get(`[data-cy="createdBy"]`).type('yowza cafe greatly');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'yowza cafe greatly');

      cy.get(`[data-cy="lastModifiedBy"]`).type('who');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'who');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creatorId"]`).type('14714');
      cy.get(`[data-cy="creatorId"]`).should('have.value', '14714');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        videoStory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', videoStoryPageUrlPattern);
    });
  });
});
