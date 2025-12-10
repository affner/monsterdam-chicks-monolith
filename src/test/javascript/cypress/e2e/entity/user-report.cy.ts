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

describe('UserReport e2e test', () => {
  const userReportPageUrl = '/user-report';
  const userReportPageUrlPattern = new RegExp('/user-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userReportSample = {
    status: 'ACTION_TAKEN',
    createdDate: '2025-12-10T02:13:49.046Z',
    isDeleted: false,
    reportCategory: 'MESSAGE_REPORT',
    reporterId: 738,
    reportedId: 1503,
  };

  let userReport;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-reports/${userReport.id}`,
      }).then(() => {
        userReport = undefined;
      });
    }
  });

  it('UserReports menu should load UserReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserReport').should('exist');
    cy.url().should('match', userReportPageUrlPattern);
  });

  describe('UserReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserReport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-report/new$'));
        cy.getEntityCreateUpdateHeading('UserReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-reports',
          body: userReportSample,
        }).then(({ body }) => {
          userReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-reports?page=0&size=20>; rel="last",<http://localhost/api/user-reports?page=0&size=20>; rel="first"',
              },
              body: [userReport],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userReport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });

      it('edit button click should load edit UserReport page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });

      it('edit button click should load edit UserReport page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserReport');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);
      });

      it('last delete button click should delete instance of UserReport', () => {
        cy.intercept('GET', '/api/user-reports/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userReportPageUrlPattern);

        userReport = undefined;
      });
    });
  });

  describe('new UserReport page', () => {
    beforeEach(() => {
      cy.visit(`${userReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserReport');
    });

    it('should create an instance of UserReport', () => {
      cy.get(`[data-cy="reportDescription"]`).type('charter motor antagonize');
      cy.get(`[data-cy="reportDescription"]`).should('have.value', 'charter motor antagonize');

      cy.get(`[data-cy="status"]`).select('REVIEWED');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T05:15');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T05:15');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T21:49');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T21:49');

      cy.get(`[data-cy="createdBy"]`).type('elastic reservation popularize');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'elastic reservation popularize');

      cy.get(`[data-cy="lastModifiedBy"]`).type('till crossly wetly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'till crossly wetly');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="reportCategory"]`).select('MESSAGE_REPORT');

      cy.get(`[data-cy="reporterId"]`).type('5581');
      cy.get(`[data-cy="reporterId"]`).should('have.value', '5581');

      cy.get(`[data-cy="reportedId"]`).type('23381');
      cy.get(`[data-cy="reportedId"]`).should('have.value', '23381');

      cy.get(`[data-cy="multimediaId"]`).type('5986');
      cy.get(`[data-cy="multimediaId"]`).should('have.value', '5986');

      cy.get(`[data-cy="messageId"]`).type('28372');
      cy.get(`[data-cy="messageId"]`).should('have.value', '28372');

      cy.get(`[data-cy="postId"]`).type('13802');
      cy.get(`[data-cy="postId"]`).should('have.value', '13802');

      cy.get(`[data-cy="commentId"]`).type('29414');
      cy.get(`[data-cy="commentId"]`).should('have.value', '29414');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userReport = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userReportPageUrlPattern);
    });
  });
});
