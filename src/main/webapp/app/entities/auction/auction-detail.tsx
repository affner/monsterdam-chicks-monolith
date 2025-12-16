import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './auction.reducer';

export const AuctionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const auctionEntity = useAppSelector(state => state.auction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="auctionDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.auction.detail.title">Auction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="monsterdamChicksApp.auction.title">Title</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="monsterdamChicksApp.auction.description">Description</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.description}</dd>
          <dt>
            <span id="startingPrice">
              <Translate contentKey="monsterdamChicksApp.auction.startingPrice">Starting Price</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.startingPrice}</dd>
          <dt>
            <span id="currentPrice">
              <Translate contentKey="monsterdamChicksApp.auction.currentPrice">Current Price</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.currentPrice}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="monsterdamChicksApp.auction.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.startDate ? <TextFormat value={auctionEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="monsterdamChicksApp.auction.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.endDate ? <TextFormat value={auctionEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="auctionStatus">
              <Translate contentKey="monsterdamChicksApp.auction.auctionStatus">Auction Status</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.auctionStatus}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.auction.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {auctionEntity.createdDate ? <TextFormat value={auctionEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.auction.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {auctionEntity.lastModifiedDate ? (
              <TextFormat value={auctionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.auction.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.auction.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{auctionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.auction.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {auctionEntity.deletedDate ? <TextFormat value={auctionEntity.deletedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.auction.creator">Creator</Translate>
          </dt>
          <dd>{auctionEntity.creator ? auctionEntity.creator.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.auction.winner">Winner</Translate>
          </dt>
          <dd>{auctionEntity.winner ? auctionEntity.winner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/auction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/auction/${auctionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AuctionDetail;
