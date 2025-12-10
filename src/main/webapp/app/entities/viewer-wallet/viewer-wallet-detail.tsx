import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './viewer-wallet.reducer';

export const ViewerWalletDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const viewerWalletEntity = useAppSelector(state => state.viewerWallet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="viewerWalletDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.viewerWallet.detail.title">ViewerWallet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{viewerWalletEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="monsterdamChicksApp.viewerWallet.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{viewerWalletEntity.amount}</dd>
          <dt>
            <span id="transactionType">
              <Translate contentKey="monsterdamChicksApp.viewerWallet.transactionType">Transaction Type</Translate>
            </span>
          </dt>
          <dd>{viewerWalletEntity.transactionType}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.viewerWallet.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {viewerWalletEntity.createdDate ? (
              <TextFormat value={viewerWalletEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.viewerWallet.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {viewerWalletEntity.lastModifiedDate ? (
              <TextFormat value={viewerWalletEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.viewerWallet.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{viewerWalletEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.viewerWallet.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{viewerWalletEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.viewerWallet.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{viewerWalletEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="viewerId">
              <Translate contentKey="monsterdamChicksApp.viewerWallet.viewerId">Viewer Id</Translate>
            </span>
          </dt>
          <dd>{viewerWalletEntity.viewerId}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.viewerWallet.payment">Payment</Translate>
          </dt>
          <dd>{viewerWalletEntity.payment ? viewerWalletEntity.payment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/viewer-wallet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/viewer-wallet/${viewerWalletEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ViewerWalletDetail;
