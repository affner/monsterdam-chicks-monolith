import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-lite.reducer';

export const UserLiteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userLiteEntity = useAppSelector(state => state.userLite.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userLiteDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.userLite.detail.title">UserLite</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.id}</dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="monsterdamChicksApp.userLite.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="monsterdamChicksApp.userLite.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {userLiteEntity.birthDate ? <TextFormat value={userLiteEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="gender">
              <Translate contentKey="monsterdamChicksApp.userLite.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.gender}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.userLite.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userLiteEntity.createdDate ? <TextFormat value={userLiteEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.userLite.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userLiteEntity.lastModifiedDate ? (
              <TextFormat value={userLiteEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.userLite.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.userLite.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.userLite.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="nickName">
              <Translate contentKey="monsterdamChicksApp.userLite.nickName">Nick Name</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.nickName}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="monsterdamChicksApp.userLite.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{userLiteEntity.fullName}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.userLite.profile">Profile</Translate>
          </dt>
          <dd>{userLiteEntity.profile ? userLiteEntity.profile.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.userLite.settings">Settings</Translate>
          </dt>
          <dd>{userLiteEntity.settings ? userLiteEntity.settings.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.userLite.countryOfBirth">Country Of Birth</Translate>
          </dt>
          <dd>{userLiteEntity.countryOfBirth ? userLiteEntity.countryOfBirth.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-lite" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-lite/${userLiteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserLiteDetail;
