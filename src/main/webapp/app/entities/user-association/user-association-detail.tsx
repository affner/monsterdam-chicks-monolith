import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-association.reducer';

export const UserAssociationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userAssociationEntity = useAppSelector(state => state.userAssociation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userAssociationDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.userAssociation.detail.title">UserAssociation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userAssociationEntity.id}</dd>
          <dt>
            <span id="requestedDate">
              <Translate contentKey="monsterdamChicksApp.userAssociation.requestedDate">Requested Date</Translate>
            </span>
          </dt>
          <dd>
            {userAssociationEntity.requestedDate ? (
              <TextFormat value={userAssociationEntity.requestedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="monsterdamChicksApp.userAssociation.status">Status</Translate>
            </span>
          </dt>
          <dd>{userAssociationEntity.status}</dd>
          <dt>
            <span id="associationToken">
              <Translate contentKey="monsterdamChicksApp.userAssociation.associationToken">Association Token</Translate>
            </span>
          </dt>
          <dd>{userAssociationEntity.associationToken}</dd>
          <dt>
            <span id="expiryDate">
              <Translate contentKey="monsterdamChicksApp.userAssociation.expiryDate">Expiry Date</Translate>
            </span>
          </dt>
          <dd>
            {userAssociationEntity.expiryDate ? (
              <TextFormat value={userAssociationEntity.expiryDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.userAssociation.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userAssociationEntity.createdDate ? (
              <TextFormat value={userAssociationEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.userAssociation.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userAssociationEntity.lastModifiedDate ? (
              <TextFormat value={userAssociationEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.userAssociation.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userAssociationEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.userAssociation.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userAssociationEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.userAssociation.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {userAssociationEntity.deletedDate ? (
              <TextFormat value={userAssociationEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.userAssociation.owner">Owner</Translate>
          </dt>
          <dd>{userAssociationEntity.owner ? userAssociationEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-association" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-association/${userAssociationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserAssociationDetail;
