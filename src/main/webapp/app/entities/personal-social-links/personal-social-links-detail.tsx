import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './personal-social-links.reducer';

export const PersonalSocialLinksDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const personalSocialLinksEntity = useAppSelector(state => state.personalSocialLinks.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personalSocialLinksDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.personalSocialLinks.detail.title">PersonalSocialLinks</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.id}</dd>
          <dt>
            <span id="normalImageS3Key">
              <Translate contentKey="monsterdamChicksApp.personalSocialLinks.normalImageS3Key">Normal Image S 3 Key</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.normalImageS3Key}</dd>
          <dt>
            <span id="thumbnailIconS3Key">
              <Translate contentKey="monsterdamChicksApp.personalSocialLinks.thumbnailIconS3Key">Thumbnail Icon S 3 Key</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.thumbnailIconS3Key}</dd>
          <dt>
            <span id="socialLink">
              <Translate contentKey="monsterdamChicksApp.personalSocialLinks.socialLink">Social Link</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.socialLink}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.personalSocialLinks.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {personalSocialLinksEntity.createdDate ? (
              <TextFormat value={personalSocialLinksEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.personalSocialLinks.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {personalSocialLinksEntity.lastModifiedDate ? (
              <TextFormat value={personalSocialLinksEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.personalSocialLinks.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.personalSocialLinks.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.personalSocialLinks.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{personalSocialLinksEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.personalSocialLinks.socialNetwork">Social Network</Translate>
          </dt>
          <dd>{personalSocialLinksEntity.socialNetwork ? personalSocialLinksEntity.socialNetwork.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.personalSocialLinks.userProfile">User Profile</Translate>
          </dt>
          <dd>{personalSocialLinksEntity.userProfile ? personalSocialLinksEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/personal-social-links" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/personal-social-links/${personalSocialLinksEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonalSocialLinksDetail;
