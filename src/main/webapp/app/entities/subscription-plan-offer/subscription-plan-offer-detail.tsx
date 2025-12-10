import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subscription-plan-offer.reducer';

export const SubscriptionPlanOfferDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subscriptionPlanOfferEntity = useAppSelector(state => state.subscriptionPlanOffer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subscriptionPlanOfferDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.detail.title">SubscriptionPlanOffer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subscriptionPlanOfferEntity.id}</dd>
          <dt>
            <span id="freeDaysDuration">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.freeDaysDuration">Free Days Duration</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionPlanOfferEntity.freeDaysDuration ? <DurationFormat value={subscriptionPlanOfferEntity.freeDaysDuration} /> : null}{' '}
            ({subscriptionPlanOfferEntity.freeDaysDuration})
          </dd>
          <dt>
            <span id="discountPercentage">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.discountPercentage">Discount Percentage</Translate>
            </span>
          </dt>
          <dd>{subscriptionPlanOfferEntity.discountPercentage}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionPlanOfferEntity.startDate ? (
              <TextFormat value={subscriptionPlanOfferEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionPlanOfferEntity.endDate ? (
              <TextFormat value={subscriptionPlanOfferEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="subscriptionsLimit">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.subscriptionsLimit">Subscriptions Limit</Translate>
            </span>
          </dt>
          <dd>{subscriptionPlanOfferEntity.subscriptionsLimit}</dd>
          <dt>
            <span id="promotionType">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.promotionType">Promotion Type</Translate>
            </span>
          </dt>
          <dd>{subscriptionPlanOfferEntity.promotionType}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionPlanOfferEntity.createdDate ? (
              <TextFormat value={subscriptionPlanOfferEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionPlanOfferEntity.lastModifiedDate ? (
              <TextFormat value={subscriptionPlanOfferEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{subscriptionPlanOfferEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{subscriptionPlanOfferEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{subscriptionPlanOfferEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.subscriptionPlanOffer.creator">Creator</Translate>
          </dt>
          <dd>{subscriptionPlanOfferEntity.creator ? subscriptionPlanOfferEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/subscription-plan-offer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-plan-offer/${subscriptionPlanOfferEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubscriptionPlanOfferDetail;
