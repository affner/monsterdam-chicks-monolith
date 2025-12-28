import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './single-video.reducer';

const resolveVideoSource = (contentS3Key?: string | null, contentContentType?: string | null, content?: string | null) => {
  if (contentS3Key) {
    if (contentS3Key.startsWith('http') || contentS3Key.startsWith('/')) {
      return contentS3Key;
    }
    return `/content/videos/${contentS3Key}`;
  }

  if (content && contentContentType) {
    return `data:${contentContentType};base64,${content}`;
  }

  return null;
};

export const SingleVideoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const singleVideoEntity = useAppSelector(state => state.singleVideo.entity);
  const videoSource = resolveVideoSource(singleVideoEntity.contentS3Key, singleVideoEntity.contentContentType, singleVideoEntity.content);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="singleVideoDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.singleVideo.detail.title">SingleVideo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.id}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="monsterdamChicksApp.singleVideo.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.thumbnail ? (
              <div>
                {singleVideoEntity.thumbnailContentType ? (
                  <a onClick={openFile(singleVideoEntity.thumbnailContentType, singleVideoEntity.thumbnail)}>
                    <img
                      src={`data:${singleVideoEntity.thumbnailContentType};base64,${singleVideoEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {singleVideoEntity.thumbnailContentType}, {byteSize(singleVideoEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="monsterdamChicksApp.singleVideo.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="monsterdamChicksApp.singleVideo.content">Content</Translate>
            </span>
          </dt>
          <dd>
            {videoSource ? (
              <video controls preload="metadata" style={{ maxWidth: '100%' }}>
                <source src={videoSource} type={singleVideoEntity.contentContentType || 'video/mp4'} />
                Your browser does not support the video tag.
              </video>
            ) : null}
          </dd>
          <dt>
            <span id="contentS3Key">
              <Translate contentKey="monsterdamChicksApp.singleVideo.contentS3Key">Content S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.contentS3Key}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="monsterdamChicksApp.singleVideo.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.duration ? <DurationFormat value={singleVideoEntity.duration} /> : null} ({singleVideoEntity.duration})
          </dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="monsterdamChicksApp.singleVideo.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.likeCount}</dd>
          <dt>
            <span id="isPreview">
              <Translate contentKey="monsterdamChicksApp.singleVideo.isPreview">Is Preview</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.isPreview ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.singleVideo.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.createdDate ? (
              <TextFormat value={singleVideoEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.singleVideo.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.lastModifiedDate ? (
              <TextFormat value={singleVideoEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.singleVideo.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.singleVideo.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{singleVideoEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.singleVideo.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {singleVideoEntity.deletedDate ? (
              <TextFormat value={singleVideoEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.singleVideo.contentPackage">Content Package</Translate>
          </dt>
          <dd>{singleVideoEntity.contentPackage ? singleVideoEntity.contentPackage.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.singleVideo.creator">Creator</Translate>
          </dt>
          <dd>{singleVideoEntity.creator ? singleVideoEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/single-video" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/single-video/${singleVideoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SingleVideoDetail;
