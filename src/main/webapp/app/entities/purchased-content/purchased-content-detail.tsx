import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './purchased-content.reducer';

export const PurchasedContentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const purchasedContentEntity = useAppSelector(state => state.purchasedContent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="purchasedContentDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.purchasedContent.detail.title">PurchasedContent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{purchasedContentEntity.id}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="monsterdamChicksApp.purchasedContent.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{purchasedContentEntity.rating}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.purchasedContent.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedContentEntity.createdDate ? (
              <TextFormat value={purchasedContentEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.purchasedContent.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedContentEntity.lastModifiedDate ? (
              <TextFormat value={purchasedContentEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.purchasedContent.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{purchasedContentEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.purchasedContent.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{purchasedContentEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.purchasedContent.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {purchasedContentEntity.deletedDate ? (
              <TextFormat value={purchasedContentEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.purchasedContent.contentPackage">Content Package</Translate>
          </dt>
          <dd>{purchasedContentEntity.contentPackage ? purchasedContentEntity.contentPackage.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.purchasedContent.payment">Payment</Translate>
          </dt>
          <dd>{purchasedContentEntity.payment ? purchasedContentEntity.payment.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.purchasedContent.viewer">Viewer</Translate>
          </dt>
          <dd>{purchasedContentEntity.viewer ? purchasedContentEntity.viewer.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.purchasedContent.creator">Creator</Translate>
          </dt>
          <dd>{purchasedContentEntity.creator ? purchasedContentEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/purchased-content" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/purchased-content/${purchasedContentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PurchasedContentDetail;
