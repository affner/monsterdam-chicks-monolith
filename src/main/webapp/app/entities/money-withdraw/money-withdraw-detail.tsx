import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './money-withdraw.reducer';

export const MoneyWithdrawDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const moneyWithdrawEntity = useAppSelector(state => state.moneyWithdraw.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="moneyWithdrawDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.moneyWithdraw.detail.title">MoneyWithdraw</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{moneyWithdrawEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="monsterdamChicksApp.moneyWithdraw.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{moneyWithdrawEntity.amount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.moneyWithdraw.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyWithdrawEntity.createdDate ? (
              <TextFormat value={moneyWithdrawEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.moneyWithdraw.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyWithdrawEntity.lastModifiedDate ? (
              <TextFormat value={moneyWithdrawEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.moneyWithdraw.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{moneyWithdrawEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.moneyWithdraw.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{moneyWithdrawEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.moneyWithdraw.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{moneyWithdrawEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="withdrawStatus">
              <Translate contentKey="monsterdamChicksApp.moneyWithdraw.withdrawStatus">Withdraw Status</Translate>
            </span>
          </dt>
          <dd>{moneyWithdrawEntity.withdrawStatus}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.moneyWithdraw.creator">Creator</Translate>
          </dt>
          <dd>{moneyWithdrawEntity.creator ? moneyWithdrawEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/money-withdraw" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/money-withdraw/${moneyWithdrawEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MoneyWithdrawDetail;
