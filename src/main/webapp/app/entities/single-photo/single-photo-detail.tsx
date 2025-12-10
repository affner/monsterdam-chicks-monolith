import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './single-photo.reducer';

export const SinglePhotoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const singlePhotoEntity = useAppSelector(state => state.singlePhoto.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="singlePhotoDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.singlePhoto.detail.title">SinglePhoto</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.id}</dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="contentS3Key">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.contentS3Key">Content S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.contentS3Key}</dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.likeCount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {singlePhotoEntity.createdDate ? (
              <TextFormat value={singlePhotoEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {singlePhotoEntity.lastModifiedDate ? (
              <TextFormat value={singlePhotoEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="creatorId">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.creatorId">Creator Id</Translate>
            </span>
          </dt>
          <dd>{singlePhotoEntity.creatorId}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.singlePhoto.contentPackage">Content Package</Translate>
          </dt>
          <dd>{singlePhotoEntity.contentPackage ? singlePhotoEntity.contentPackage.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/single-photo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/single-photo/${singlePhotoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SinglePhotoDetail;
