import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ledger-entry.reducer';

export const LedgerEntryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ledgerEntryEntity = useAppSelector(state => state.ledgerEntry.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ledgerEntryDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.ledgerEntry.detail.title">LedgerEntry</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.currency}</dd>
          <dt>
            <span id="entryType">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.entryType">Entry Type</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.entryType}</dd>
          <dt>
            <span id="accountType">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.accountType">Account Type</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.accountType}</dd>
          <dt>
            <span id="accountOwnerId">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.accountOwnerId">Account Owner Id</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.accountOwnerId}</dd>
          <dt>
            <span id="reason">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.reason">Reason</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.reason}</dd>
          <dt>
            <span id="referenceType">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.referenceType">Reference Type</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.referenceType}</dd>
          <dt>
            <span id="referenceId">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.referenceId">Reference Id</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.referenceId}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {ledgerEntryEntity.createdDate ? (
              <TextFormat value={ledgerEntryEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{ledgerEntryEntity.createdBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {ledgerEntryEntity.deletedDate ? (
              <TextFormat value={ledgerEntryEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/ledger-entry" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ledger-entry/${ledgerEntryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LedgerEntryDetail;
