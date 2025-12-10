import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './video-story.reducer';

export const VideoStoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const videoStoryEntity = useAppSelector(state => state.videoStory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="videoStoryDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.videoStory.detail.title">VideoStory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{videoStoryEntity.id}</dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="monsterdamChicksApp.videoStory.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{videoStoryEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="contentS3Key">
              <Translate contentKey="monsterdamChicksApp.videoStory.contentS3Key">Content S 3 Key</Translate>
            </span>
          </dt>
          <dd>{videoStoryEntity.contentS3Key}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="monsterdamChicksApp.videoStory.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {videoStoryEntity.duration ? <DurationFormat value={videoStoryEntity.duration} /> : null} ({videoStoryEntity.duration})
          </dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="monsterdamChicksApp.videoStory.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{videoStoryEntity.likeCount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.videoStory.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {videoStoryEntity.createdDate ? <TextFormat value={videoStoryEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.videoStory.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {videoStoryEntity.lastModifiedDate ? (
              <TextFormat value={videoStoryEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.videoStory.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{videoStoryEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.videoStory.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{videoStoryEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.videoStory.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{videoStoryEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="creatorId">
              <Translate contentKey="monsterdamChicksApp.videoStory.creatorId">Creator Id</Translate>
            </span>
          </dt>
          <dd>{videoStoryEntity.creatorId}</dd>
        </dl>
        <Button tag={Link} to="/video-story" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/video-story/${videoStoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VideoStoryDetail;
