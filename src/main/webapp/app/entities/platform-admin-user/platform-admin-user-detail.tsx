import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './platform-admin-user.reducer';

export const PlatformAdminUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const platformAdminUserEntity = useAppSelector(state => state.platformAdminUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="platformAdminUserDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.platformAdminUser.detail.title">PlatformAdminUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{platformAdminUserEntity.id}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{platformAdminUserEntity.fullName}</dd>
          <dt>
            <span id="emailAddress">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.emailAddress">Email Address</Translate>
            </span>
          </dt>
          <dd>{platformAdminUserEntity.emailAddress}</dd>
          <dt>
            <span id="nickName">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.nickName">Nick Name</Translate>
            </span>
          </dt>
          <dd>{platformAdminUserEntity.nickName}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{platformAdminUserEntity.gender}</dd>
          <dt>
            <span id="mobilePhone">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.mobilePhone">Mobile Phone</Translate>
            </span>
          </dt>
          <dd>{platformAdminUserEntity.mobilePhone}</dd>
          <dt>
            <span id="lastLoginDate">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.lastLoginDate">Last Login Date</Translate>
            </span>
          </dt>
          <dd>
            {platformAdminUserEntity.lastLoginDate ? (
              <TextFormat value={platformAdminUserEntity.lastLoginDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {platformAdminUserEntity.birthDate ? (
              <TextFormat value={platformAdminUserEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {platformAdminUserEntity.createdDate ? (
              <TextFormat value={platformAdminUserEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {platformAdminUserEntity.lastModifiedDate ? (
              <TextFormat value={platformAdminUserEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{platformAdminUserEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{platformAdminUserEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.platformAdminUser.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{platformAdminUserEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/platform-admin-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/platform-admin-user/${platformAdminUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlatformAdminUserDetail;
