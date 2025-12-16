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
  const directMessageSample = { messageContent: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', createdDate: '2025-12-15T13:22:56.811Z' };

  let directMessage;
  let chatRoom;
  let userLite;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/chat-rooms',
      body: {
        roomType: 'DIRECT',
        title: 'gladly unless',
        lastAction: 'out rosin or',
        lastConnectionDate: '2025-12-15T12:20:38.270Z',
        createdDate: '2025-12-15T22:53:26.410Z',
        lastModifiedDate: '2025-12-15T22:30:11.891Z',
        createdBy: 'overcharge',
        lastModifiedBy: 'flickering',
        deletedDate: '2025-12-15T21:59:22.286Z',
      },
    }).then(({ body }) => {
      chatRoom = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-lites',
      body: {
        thumbnailS3Key: 'once whereas chainstay',
        birthDate: '2025-12-15',
        gender: 'FEMALE',
        createdDate: '2025-12-15T08:58:06.450Z',
        lastModifiedDate: '2025-12-16T01:30:35.124Z',
        createdBy: 'charlatan',
        lastModifiedBy: 'valley sadly crank',
        deletedDate: '2025-12-16T04:09:07.443Z',
        nickName: 'gyfqxme0',
        fullName: 'efAk',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/direct-messages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/direct-messages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/direct-messages/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/direct-messages', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/chat-rooms', {
      statusCode: 200,
      body: [chatRoom],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
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

  afterEach(() => {
    if (chatRoom) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/chat-rooms/${chatRoom.id}`,
      }).then(() => {
        chatRoom = undefined;
      });
    }
    if (userLite) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-lites/${userLite.id}`,
      }).then(() => {
        userLite = undefined;
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
          body: {
            ...directMessageSample,
            chatRoom,
            sender: userLite,
          },
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

      cy.get(`[data-cy="readDate"]`).type('2025-12-15T19:52');
      cy.get(`[data-cy="readDate"]`).blur();
      cy.get(`[data-cy="readDate"]`).should('have.value', '2025-12-15T19:52');

      cy.get(`[data-cy="likeCount"]`).type('12568');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '12568');

      cy.get(`[data-cy="isHidden"]`).should('not.be.checked');
      cy.get(`[data-cy="isHidden"]`).click();
      cy.get(`[data-cy="isHidden"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T13:54');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T13:54');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T10:14');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T10:14');

      cy.get(`[data-cy="createdBy"]`).type('hmph');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'hmph');

      cy.get(`[data-cy="lastModifiedBy"]`).type('factorize descriptive');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'factorize descriptive');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T18:16');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T18:16');

      cy.get(`[data-cy="repliedStoryId"]`).type('7529');
      cy.get(`[data-cy="repliedStoryId"]`).should('have.value', '7529');

      cy.get(`[data-cy="chatRoom"]`).select(1);
      cy.get(`[data-cy="sender"]`).select(1);

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
