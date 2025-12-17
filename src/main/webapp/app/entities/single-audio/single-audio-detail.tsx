import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './single-audio.reducer';

export const SingleAudioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const singleAudioEntity = useAppSelector(state => state.singleAudio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="singleAudioDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.singleAudio.detail.title">SingleAudio</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.id}</dd>
          <dt>
            <span id="thumbnail">
              <Translate contentKey="monsterdamChicksApp.singleAudio.thumbnail">Thumbnail</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.thumbnail ? (
              <div>
                {singleAudioEntity.thumbnailContentType ? (
                  <a onClick={openFile(singleAudioEntity.thumbnailContentType, singleAudioEntity.thumbnail)}>
                    <img
                      src={`data:${singleAudioEntity.thumbnailContentType};base64,${singleAudioEntity.thumbnail}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {singleAudioEntity.thumbnailContentType}, {byteSize(singleAudioEntity.thumbnail)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="thumbnailS3Key">
              <Translate contentKey="monsterdamChicksApp.singleAudio.thumbnailS3Key">Thumbnail S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.thumbnailS3Key}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="monsterdamChicksApp.singleAudio.content">Content</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.content ? (
              <div>
                {singleAudioEntity.contentContentType ? (
                  <a onClick={openFile(singleAudioEntity.contentContentType, singleAudioEntity.content)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {singleAudioEntity.contentContentType}, {byteSize(singleAudioEntity.content)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="contentS3Key">
              <Translate contentKey="monsterdamChicksApp.singleAudio.contentS3Key">Content S 3 Key</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.contentS3Key}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="monsterdamChicksApp.singleAudio.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.duration ? <DurationFormat value={singleAudioEntity.duration} /> : null} ({singleAudioEntity.duration})
          </dd>
          <dt>
            <span id="isPreview">
              <Translate contentKey="monsterdamChicksApp.singleAudio.isPreview">Is Preview</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.isPreview ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.singleAudio.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.createdDate ? (
              <TextFormat value={singleAudioEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.singleAudio.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.lastModifiedDate ? (
              <TextFormat value={singleAudioEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.singleAudio.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.singleAudio.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{singleAudioEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.singleAudio.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {singleAudioEntity.deletedDate ? (
              <TextFormat value={singleAudioEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.singleAudio.contentPackage">Content Package</Translate>
          </dt>
          <dd>{singleAudioEntity.contentPackage ? singleAudioEntity.contentPackage.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.singleAudio.creator">Creator</Translate>
          </dt>
          <dd>{singleAudioEntity.creator ? singleAudioEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/single-audio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/single-audio/${singleAudioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SingleAudioDetail;
