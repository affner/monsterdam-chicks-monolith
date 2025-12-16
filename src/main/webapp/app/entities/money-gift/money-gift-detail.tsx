import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './money-gift.reducer';

export const MoneyGiftDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const moneyGiftEntity = useAppSelector(state => state.moneyGift.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="moneyGiftDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.moneyGift.detail.title">MoneyGift</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{moneyGiftEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="monsterdamChicksApp.moneyGift.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{moneyGiftEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="monsterdamChicksApp.moneyGift.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{moneyGiftEntity.currency}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.moneyGift.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyGiftEntity.createdDate ? <TextFormat value={moneyGiftEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.moneyGift.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyGiftEntity.lastModifiedDate ? (
              <TextFormat value={moneyGiftEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.moneyGift.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{moneyGiftEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.moneyGift.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{moneyGiftEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.moneyGift.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {moneyGiftEntity.deletedDate ? <TextFormat value={moneyGiftEntity.deletedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="messageId">
              <Translate contentKey="monsterdamChicksApp.moneyGift.messageId">Message Id</Translate>
            </span>
          </dt>
          <dd>{moneyGiftEntity.messageId}</dd>
          <dt>
            <span id="postId">
              <Translate contentKey="monsterdamChicksApp.moneyGift.postId">Post Id</Translate>
            </span>
          </dt>
          <dd>{moneyGiftEntity.postId}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.moneyGift.payment">Payment</Translate>
          </dt>
          <dd>{moneyGiftEntity.payment ? moneyGiftEntity.payment.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.moneyGift.viewer">Viewer</Translate>
          </dt>
          <dd>{moneyGiftEntity.viewer ? moneyGiftEntity.viewer.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.moneyGift.creator">Creator</Translate>
          </dt>
          <dd>{moneyGiftEntity.creator ? moneyGiftEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/money-gift" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/money-gift/${moneyGiftEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MoneyGiftDetail;
