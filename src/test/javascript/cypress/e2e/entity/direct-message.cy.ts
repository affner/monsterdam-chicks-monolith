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

describe('DirectMessage e2e test', () => {
  const directMessagePageUrl = '/direct-message';
  const directMessagePageUrlPattern = new RegExp('/direct-message(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const directMessageSample = {
    messageContent: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    createdDate: '2025-12-09T23:57:53.291Z',
    isDeleted: false,
    senderId: 18121,
  };

  let directMessage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/direct-messages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/direct-messages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/direct-messages/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (directMessage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/direct-messages/${directMessage.id}`,
      }).then(() => {
        directMessage = undefined;
      });
    }
  });

  it('DirectMessages menu should load DirectMessages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('direct-message');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DirectMessage').should('exist');
    cy.url().should('match', directMessagePageUrlPattern);
  });

  describe('DirectMessage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(directMessagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DirectMessage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/direct-message/new$'));
        cy.getEntityCreateUpdateHeading('DirectMessage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/direct-messages',
          body: directMessageSample,
        }).then(({ body }) => {
          directMessage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/direct-messages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/direct-messages?page=0&size=20>; rel="last",<http://localhost/api/direct-messages?page=0&size=20>; rel="first"',
              },
              body: [directMessage],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(directMessagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DirectMessage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('directMessage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);
      });

      it('edit button click should load edit DirectMessage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DirectMessage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);
      });

      it('edit button click should load edit DirectMessage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DirectMessage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);
      });

      it('last delete button click should delete instance of DirectMessage', () => {
        cy.intercept('GET', '/api/direct-messages/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('directMessage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', directMessagePageUrlPattern);

        directMessage = undefined;
      });
    });
  });

  describe('new DirectMessage page', () => {
    beforeEach(() => {
      cy.visit(`${directMessagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DirectMessage');
    });

    it('should create an instance of DirectMessage', () => {
      cy.get(`[data-cy="messageContent"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="messageContent"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="readDate"]`).type('2025-12-09T19:26');
      cy.get(`[data-cy="readDate"]`).blur();
      cy.get(`[data-cy="readDate"]`).should('have.value', '2025-12-09T19:26');

      cy.get(`[data-cy="likeCount"]`).type('12014');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '12014');

      cy.get(`[data-cy="isHidden"]`).should('not.be.checked');
      cy.get(`[data-cy="isHidden"]`).click();
      cy.get(`[data-cy="isHidden"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T13:33');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T13:33');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T10:36');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T10:36');

      cy.get(`[data-cy="createdBy"]`).type('though');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'though');

      cy.get(`[data-cy="lastModifiedBy"]`).type('madly leap');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'madly leap');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="repliedStoryId"]`).type('8831');
      cy.get(`[data-cy="repliedStoryId"]`).should('have.value', '8831');

      cy.get(`[data-cy="senderId"]`).type('3938');
      cy.get(`[data-cy="senderId"]`).should('have.value', '3938');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        directMessage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', directMessagePageUrlPattern);
    });
  });
});
