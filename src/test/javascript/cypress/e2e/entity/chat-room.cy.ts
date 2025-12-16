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

describe('ChatRoom e2e test', () => {
  const chatRoomPageUrl = '/chat-room';
  const chatRoomPageUrlPattern = new RegExp('/chat-room(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const chatRoomSample = { roomType: 'GROUP', createdDate: '2025-12-15T23:58:47.568Z' };

  let chatRoom;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/chat-rooms+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/chat-rooms').as('postEntityRequest');
    cy.intercept('DELETE', '/api/chat-rooms/*').as('deleteEntityRequest');
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
  });

  it('ChatRooms menu should load ChatRooms page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('chat-room');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChatRoom').should('exist');
    cy.url().should('match', chatRoomPageUrlPattern);
  });

  describe('ChatRoom page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(chatRoomPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChatRoom page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/chat-room/new$'));
        cy.getEntityCreateUpdateHeading('ChatRoom');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/chat-rooms',
          body: chatRoomSample,
        }).then(({ body }) => {
          chatRoom = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/chat-rooms+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/chat-rooms?page=0&size=20>; rel="last",<http://localhost/api/chat-rooms?page=0&size=20>; rel="first"',
              },
              body: [chatRoom],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(chatRoomPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChatRoom page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('chatRoom');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);
      });

      it('edit button click should load edit ChatRoom page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatRoom');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);
      });

      it('edit button click should load edit ChatRoom page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChatRoom');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);
      });

      it('last delete button click should delete instance of ChatRoom', () => {
        cy.intercept('GET', '/api/chat-rooms/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('chatRoom').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chatRoomPageUrlPattern);

        chatRoom = undefined;
      });
    });
  });

  describe('new ChatRoom page', () => {
    beforeEach(() => {
      cy.visit(`${chatRoomPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ChatRoom');
    });

    it('should create an instance of ChatRoom', () => {
      cy.get(`[data-cy="roomType"]`).select('GROUP');

      cy.get(`[data-cy="title"]`).type('oof marimba');
      cy.get(`[data-cy="title"]`).should('have.value', 'oof marimba');

      cy.get(`[data-cy="lastAction"]`).type('blindly');
      cy.get(`[data-cy="lastAction"]`).should('have.value', 'blindly');

      cy.get(`[data-cy="lastConnectionDate"]`).type('2025-12-16T03:01');
      cy.get(`[data-cy="lastConnectionDate"]`).blur();
      cy.get(`[data-cy="lastConnectionDate"]`).should('have.value', '2025-12-16T03:01');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-16T02:09');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-16T02:09');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T13:05');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T13:05');

      cy.get(`[data-cy="createdBy"]`).type('pop consequently');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'pop consequently');

      cy.get(`[data-cy="lastModifiedBy"]`).type('faithfully blink');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'faithfully blink');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-16T02:29');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-16T02:29');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        chatRoom = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', chatRoomPageUrlPattern);
    });
  });
});
