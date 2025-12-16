import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.userProfile.detail.title">UserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="emailContact">
              <Translate contentKey="monsterdamChicksApp.userProfile.emailContact">Email Contact</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.emailContact}</dd>
          <dt>
            <span id="profilePhotoS3Key">
              <Translate contentKey="monsterdamChicksApp.userProfile.profilePhotoS3Key">Profile Photo S 3 Key</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.profilePhotoS3Key}</dd>
          <dt>
            <span id="coverPhotoS3Key">
              <Translate contentKey="monsterdamChicksApp.userProfile.coverPhotoS3Key">Cover Photo S 3 Key</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.coverPhotoS3Key}</dd>
          <dt>
            <span id="mainContentUrl">
              <Translate contentKey="monsterdamChicksApp.userProfile.mainContentUrl">Main Content Url</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.mainContentUrl}</dd>
          <dt>
            <span id="mobilePhone">
              <Translate contentKey="monsterdamChicksApp.userProfile.mobilePhone">Mobile Phone</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.mobilePhone}</dd>
          <dt>
            <span id="websiteUrl">
              <Translate contentKey="monsterdamChicksApp.userProfile.websiteUrl">Website Url</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.websiteUrl}</dd>
          <dt>
            <span id="amazonWishlistUrl">
              <Translate contentKey="monsterdamChicksApp.userProfile.amazonWishlistUrl">Amazon Wishlist Url</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.amazonWishlistUrl}</dd>
          <dt>
            <span id="lastLoginDate">
              <Translate contentKey="monsterdamChicksApp.userProfile.lastLoginDate">Last Login Date</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.lastLoginDate ? (
              <TextFormat value={userProfileEntity.lastLoginDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="biography">
              <Translate contentKey="monsterdamChicksApp.userProfile.biography">Biography</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.biography}</dd>
          <dt>
            <span id="isFree">
              <Translate contentKey="monsterdamChicksApp.userProfile.isFree">Is Free</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.isFree ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.userProfile.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.createdDate ? (
              <TextFormat value={userProfileEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.userProfile.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.lastModifiedDate ? (
              <TextFormat value={userProfileEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.userProfile.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.userProfile.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.userProfile.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.deletedDate ? (
              <TextFormat value={userProfileEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.userProfile.stateOfResidence">State Of Residence</Translate>
          </dt>
          <dd>{userProfileEntity.stateOfResidence ? userProfileEntity.stateOfResidence.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
