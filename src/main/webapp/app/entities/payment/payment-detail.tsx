import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment.reducer';

export const PaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentEntity = useAppSelector(state => state.payment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.payment.detail.title">Payment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="monsterdamChicksApp.payment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="monsterdamChicksApp.payment.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.currency}</dd>
          <dt>
            <span id="paymentDate">
              <Translate contentKey="monsterdamChicksApp.payment.paymentDate">Payment Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentEntity.paymentDate ? <TextFormat value={paymentEntity.paymentDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="paymentStatus">
              <Translate contentKey="monsterdamChicksApp.payment.paymentStatus">Payment Status</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.paymentStatus}</dd>
          <dt>
            <span id="paymentReference">
              <Translate contentKey="monsterdamChicksApp.payment.paymentReference">Payment Reference</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.paymentReference}</dd>
          <dt>
            <span id="cloudTransactionId">
              <Translate contentKey="monsterdamChicksApp.payment.cloudTransactionId">Cloud Transaction Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.cloudTransactionId}</dd>
          <dt>
            <span id="providerPaymentIntentId">
              <Translate contentKey="monsterdamChicksApp.payment.providerPaymentIntentId">Provider Payment Intent Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.providerPaymentIntentId}</dd>
          <dt>
            <span id="providerChargeId">
              <Translate contentKey="monsterdamChicksApp.payment.providerChargeId">Provider Charge Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.providerChargeId}</dd>
          <dt>
            <span id="providerCustomerId">
              <Translate contentKey="monsterdamChicksApp.payment.providerCustomerId">Provider Customer Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.providerCustomerId}</dd>
          <dt>
            <span id="providerPaymentMethodId">
              <Translate contentKey="monsterdamChicksApp.payment.providerPaymentMethodId">Provider Payment Method Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.providerPaymentMethodId}</dd>
          <dt>
            <span id="providerEventLastId">
              <Translate contentKey="monsterdamChicksApp.payment.providerEventLastId">Provider Event Last Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.providerEventLastId}</dd>
          <dt>
            <span id="countryCode">
              <Translate contentKey="monsterdamChicksApp.payment.countryCode">Country Code</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.countryCode}</dd>
          <dt>
            <span id="providerFeeAmount">
              <Translate contentKey="monsterdamChicksApp.payment.providerFeeAmount">Provider Fee Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.providerFeeAmount}</dd>
          <dt>
            <span id="platformFeeAmount">
              <Translate contentKey="monsterdamChicksApp.payment.platformFeeAmount">Platform Fee Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.platformFeeAmount}</dd>
          <dt>
            <span id="creatorNetAmount">
              <Translate contentKey="monsterdamChicksApp.payment.creatorNetAmount">Creator Net Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.creatorNetAmount}</dd>
          <dt>
            <span id="taxAmount">
              <Translate contentKey="monsterdamChicksApp.payment.taxAmount">Tax Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.taxAmount}</dd>
          <dt>
            <span id="authorizedDate">
              <Translate contentKey="monsterdamChicksApp.payment.authorizedDate">Authorized Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentEntity.authorizedDate ? <TextFormat value={paymentEntity.authorizedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="capturedDate">
              <Translate contentKey="monsterdamChicksApp.payment.capturedDate">Captured Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentEntity.capturedDate ? <TextFormat value={paymentEntity.capturedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.payment.method">Method</Translate>
          </dt>
          <dd>{paymentEntity.method ? paymentEntity.method.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.payment.provider">Provider</Translate>
          </dt>
          <dd>{paymentEntity.provider ? paymentEntity.provider.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.payment.viewer">Viewer</Translate>
          </dt>
          <dd>{paymentEntity.viewer ? paymentEntity.viewer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment/${paymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentDetail;
