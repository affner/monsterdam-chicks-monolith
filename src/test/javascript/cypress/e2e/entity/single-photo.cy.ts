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

describe('SinglePhoto e2e test', () => {
  const singlePhotoPageUrl = '/single-photo';
  const singlePhotoPageUrlPattern = new RegExp('/single-photo(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const singlePhotoSample = {
    thumbnail: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    thumbnailContentType: 'unknown',
    thumbnailS3Key: 'pretty',
    content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    contentContentType: 'unknown',
    contentS3Key: 'around',
    createdDate: '2025-12-16T03:06:17.167Z',
  };

  let singlePhoto;
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
        thumbnailS3Key: 'hence jaggedly',
        birthDate: '2025-12-15',
        gender: 'MALE',
        createdDate: '2025-12-15T14:19:24.023Z',
        lastModifiedDate: '2025-12-15T13:41:49.885Z',
        createdBy: 'huzzah minister',
        lastModifiedBy: 'supposing unused',
        deletedDate: '2025-12-15T11:43:41.338Z',
        nickName: '584',
        fullName: 'W',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/single-photos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-photos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-photos/*').as('deleteEntityRequest');
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
    if (singlePhoto) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/single-photos/${singlePhoto.id}`,
      }).then(() => {
        singlePhoto = undefined;
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

  it('SinglePhotos menu should load SinglePhotos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('single-photo');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SinglePhoto').should('exist');
    cy.url().should('match', singlePhotoPageUrlPattern);
  });

  describe('SinglePhoto page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(singlePhotoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SinglePhoto page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/single-photo/new$'));
        cy.getEntityCreateUpdateHeading('SinglePhoto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/single-photos',
          body: {
            ...singlePhotoSample,
            creator: userLite,
          },
        }).then(({ body }) => {
          singlePhoto = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/single-photos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/single-photos?page=0&size=20>; rel="last",<http://localhost/api/single-photos?page=0&size=20>; rel="first"',
              },
              body: [singlePhoto],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(singlePhotoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SinglePhoto page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('singlePhoto');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);
      });

      it('edit button click should load edit SinglePhoto page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SinglePhoto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);
      });

      it('edit button click should load edit SinglePhoto page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SinglePhoto');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);
      });

      it('last delete button click should delete instance of SinglePhoto', () => {
        cy.intercept('GET', '/api/single-photos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('singlePhoto').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singlePhotoPageUrlPattern);

        singlePhoto = undefined;
      });
    });
  });

  describe('new SinglePhoto page', () => {
    beforeEach(() => {
      cy.visit(`${singlePhotoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SinglePhoto');
    });

    it('should create an instance of SinglePhoto', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="thumbnailS3Key"]`).type('where rotten beside');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'where rotten beside');

      cy.setFieldImageAsBytesOfEntity('content', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="contentS3Key"]`).type('lest essence per');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'lest essence per');

      cy.get(`[data-cy="likeCount"]`).type('26882');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '26882');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T13:10');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T13:10');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T16:01');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T16:01');

      cy.get(`[data-cy="createdBy"]`).type('consequently');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'consequently');

      cy.get(`[data-cy="lastModifiedBy"]`).type('until milestone describe');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'until milestone describe');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T20:32');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T20:32');

      cy.get(`[data-cy="creator"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        singlePhoto = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', singlePhotoPageUrlPattern);
    });
  });
});
