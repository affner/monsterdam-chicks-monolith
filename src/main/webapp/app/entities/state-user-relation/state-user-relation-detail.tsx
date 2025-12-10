import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './state-user-relation.reducer';

export const StateUserRelationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stateUserRelationEntity = useAppSelector(state => state.stateUserRelation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stateUserRelationDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.stateUserRelation.detail.title">StateUserRelation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stateUserRelationEntity.id}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.stateUserRelation.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {stateUserRelationEntity.createdDate ? (
              <TextFormat value={stateUserRelationEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.stateUserRelation.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {stateUserRelationEntity.lastModifiedDate ? (
              <TextFormat value={stateUserRelationEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.stateUserRelation.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{stateUserRelationEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.stateUserRelation.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{stateUserRelationEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.stateUserRelation.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{stateUserRelationEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.stateUserRelation.state">State</Translate>
          </dt>
          <dd>{stateUserRelationEntity.state ? stateUserRelationEntity.state.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.stateUserRelation.userProfile">User Profile</Translate>
          </dt>
          <dd>{stateUserRelationEntity.userProfile ? stateUserRelationEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/state-user-relation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/state-user-relation/${stateUserRelationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StateUserRelationDetail;
