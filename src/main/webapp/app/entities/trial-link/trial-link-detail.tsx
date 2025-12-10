import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trial-link.reducer';

export const TrialLinkDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const trialLinkEntity = useAppSelector(state => state.trialLink.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="trialLinkDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.trialLink.detail.title">TrialLink</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{trialLinkEntity.id}</dd>
          <dt>
            <span id="linkCode">
              <Translate contentKey="monsterdamChicksApp.trialLink.linkCode">Link Code</Translate>
            </span>
          </dt>
          <dd>{trialLinkEntity.linkCode}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="monsterdamChicksApp.trialLink.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {trialLinkEntity.startDate ? <TextFormat value={trialLinkEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="monsterdamChicksApp.trialLink.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {trialLinkEntity.endDate ? <TextFormat value={trialLinkEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="freeDays">
              <Translate contentKey="monsterdamChicksApp.trialLink.freeDays">Free Days</Translate>
            </span>
          </dt>
          <dd>{trialLinkEntity.freeDays}</dd>
          <dt>
            <span id="isUsed">
              <Translate contentKey="monsterdamChicksApp.trialLink.isUsed">Is Used</Translate>
            </span>
          </dt>
          <dd>{trialLinkEntity.isUsed ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.trialLink.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {trialLinkEntity.createdDate ? <TextFormat value={trialLinkEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.trialLink.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {trialLinkEntity.lastModifiedDate ? (
              <TextFormat value={trialLinkEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.trialLink.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{trialLinkEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.trialLink.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{trialLinkEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.trialLink.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{trialLinkEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.trialLink.creator">Creator</Translate>
          </dt>
          <dd>{trialLinkEntity.creator ? trialLinkEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trial-link" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trial-link/${trialLinkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TrialLinkDetail;
