import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './refund-transaction.reducer';

export const RefundTransactionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const refundTransactionEntity = useAppSelector(state => state.refundTransaction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="refundTransactionDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.refundTransaction.detail.title">RefundTransaction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{refundTransactionEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{refundTransactionEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{refundTransactionEntity.currency}</dd>
          <dt>
            <span id="reason">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.reason">Reason</Translate>
            </span>
          </dt>
          <dd>{refundTransactionEntity.reason}</dd>
          <dt>
            <span id="paymentReference">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.paymentReference">Payment Reference</Translate>
            </span>
          </dt>
          <dd>{refundTransactionEntity.paymentReference}</dd>
          <dt>
            <span id="providerChargeId">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.providerChargeId">Provider Charge Id</Translate>
            </span>
          </dt>
          <dd>{refundTransactionEntity.providerChargeId}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.status">Status</Translate>
            </span>
          </dt>
          <dd>{refundTransactionEntity.status}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {refundTransactionEntity.createdDate ? (
              <TextFormat value={refundTransactionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="processedAt">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.processedAt">Processed At</Translate>
            </span>
          </dt>
          <dd>
            {refundTransactionEntity.processedAt ? (
              <TextFormat value={refundTransactionEntity.processedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{refundTransactionEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{refundTransactionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.refundTransaction.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {refundTransactionEntity.deletedDate ? (
              <TextFormat value={refundTransactionEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.refundTransaction.payment">Payment</Translate>
          </dt>
          <dd>{refundTransactionEntity.payment ? refundTransactionEntity.payment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/refund-transaction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/refund-transaction/${refundTransactionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RefundTransactionDetail;
