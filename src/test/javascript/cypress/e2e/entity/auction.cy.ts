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

describe('Auction e2e test', () => {
  const auctionPageUrl = '/auction';
  const auctionPageUrlPattern = new RegExp('/auction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const auctionSample = {
    title: 'um given',
    startingPrice: 11671.77,
    startDate: '2025-12-10T04:35:20.075Z',
    endDate: '2025-12-09T15:46:02.800Z',
    auctionStatus: 'ACTIVE',
    createdDate: '2025-12-10T07:03:29.243Z',
    isDeleted: false,
  };

  let auction;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/auctions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/auctions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/auctions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (auction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/auctions/${auction.id}`,
      }).then(() => {
        auction = undefined;
      });
    }
  });

  it('Auctions menu should load Auctions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('auction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Auction').should('exist');
    cy.url().should('match', auctionPageUrlPattern);
  });

  describe('Auction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(auctionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Auction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/auction/new$'));
        cy.getEntityCreateUpdateHeading('Auction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', auctionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/auctions',
          body: auctionSample,
        }).then(({ body }) => {
          auction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/auctions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/auctions?page=0&size=20>; rel="last",<http://localhost/api/auctions?page=0&size=20>; rel="first"',
              },
              body: [auction],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(auctionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Auction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('auction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', auctionPageUrlPattern);
      });

      it('edit button click should load edit Auction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Auction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', auctionPageUrlPattern);
      });

      it('edit button click should load edit Auction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Auction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', auctionPageUrlPattern);
      });

      it('last delete button click should delete instance of Auction', () => {
        cy.intercept('GET', '/api/auctions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('auction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', auctionPageUrlPattern);

        auction = undefined;
      });
    });
  });

  describe('new Auction page', () => {
    beforeEach(() => {
      cy.visit(`${auctionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Auction');
    });

    it('should create an instance of Auction', () => {
      cy.get(`[data-cy="title"]`).type('dissemble lest and');
      cy.get(`[data-cy="title"]`).should('have.value', 'dissemble lest and');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="startingPrice"]`).type('29611.12');
      cy.get(`[data-cy="startingPrice"]`).should('have.value', '29611.12');

      cy.get(`[data-cy="currentPrice"]`).type('138.64');
      cy.get(`[data-cy="currentPrice"]`).should('have.value', '138.64');

      cy.get(`[data-cy="startDate"]`).type('2025-12-09T18:33');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2025-12-09T18:33');

      cy.get(`[data-cy="endDate"]`).type('2025-12-09T20:21');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2025-12-09T20:21');

      cy.get(`[data-cy="auctionStatus"]`).select('CANCELLED');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T20:34');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T20:34');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T05:13');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T05:13');

      cy.get(`[data-cy="createdBy"]`).type('kookily that viciously');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'kookily that viciously');

      cy.get(`[data-cy="lastModifiedBy"]`).type('internationalize');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'internationalize');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        auction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', auctionPageUrlPattern);
    });
  });
});
