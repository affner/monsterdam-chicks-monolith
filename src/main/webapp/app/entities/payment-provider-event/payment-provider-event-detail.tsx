import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-provider-event.reducer';

export const PaymentProviderEventDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentProviderEventEntity = useAppSelector(state => state.paymentProviderEvent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentProviderEventDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.detail.title">PaymentProviderEvent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEventEntity.id}</dd>
          <dt>
            <span id="providerName">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.providerName">Provider Name</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEventEntity.providerName}</dd>
          <dt>
            <span id="eventType">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.eventType">Event Type</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEventEntity.eventType}</dd>
          <dt>
            <span id="eventId">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.eventId">Event Id</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEventEntity.eventId}</dd>
          <dt>
            <span id="payloadJson">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.payloadJson">Payload Json</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEventEntity.payloadJson}</dd>
          <dt>
            <span id="receivedAt">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.receivedAt">Received At</Translate>
            </span>
          </dt>
          <dd>
            {paymentProviderEventEntity.receivedAt ? (
              <TextFormat value={paymentProviderEventEntity.receivedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="processedAt">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.processedAt">Processed At</Translate>
            </span>
          </dt>
          <dd>
            {paymentProviderEventEntity.processedAt ? (
              <TextFormat value={paymentProviderEventEntity.processedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="processingStatus">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.processingStatus">Processing Status</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEventEntity.processingStatus}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEventEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEventEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentProviderEventEntity.deletedDate ? (
              <TextFormat value={paymentProviderEventEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.payment">Payment</Translate>
          </dt>
          <dd>{paymentProviderEventEntity.payment ? paymentProviderEventEntity.payment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment-provider-event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-provider-event/${paymentProviderEventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentProviderEventDetail;
