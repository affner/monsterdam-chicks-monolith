import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './money-earning.reducer';

export const MoneyEarningDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const moneyEarningEntity = useAppSelector(state => state.moneyEarning.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="moneyEarningDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.moneyEarning.detail.title">MoneyEarning</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.currency}</dd>
          <dt>
            <span id="transactionType">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.transactionType">Transaction Type</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.transactionType}</dd>
          <dt>
            <span id="sourceType">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.sourceType">Source Type</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.sourceType}</dd>
          <dt>
            <span id="sourceId">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.sourceId">Source Id</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.sourceId}</dd>
          <dt>
            <span id="isPaid">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.isPaid">Is Paid</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.isPaid ? 'true' : 'false'}</dd>
          <dt>
            <span id="payoutId">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.payoutId">Payout Id</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.payoutId}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyEarningEntity.createdDate ? (
              <TextFormat value={moneyEarningEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyEarningEntity.lastModifiedDate ? (
              <TextFormat value={moneyEarningEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{moneyEarningEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.moneyEarning.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyEarningEntity.deletedDate ? (
              <TextFormat value={moneyEarningEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.moneyEarning.creator">Creator</Translate>
          </dt>
          <dd>{moneyEarningEntity.creator ? moneyEarningEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/money-earning" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/money-earning/${moneyEarningEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MoneyEarningDetail;
