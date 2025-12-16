import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hash-tag.reducer';

export const HashTagDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hashTagEntity = useAppSelector(state => state.hashTag.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hashTagDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.hashTag.detail.title">HashTag</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hashTagEntity.id}</dd>
          <dt>
            <span id="tagName">
              <Translate contentKey="monsterdamChicksApp.hashTag.tagName">Tag Name</Translate>
            </span>
          </dt>
          <dd>{hashTagEntity.tagName}</dd>
          <dt>
            <span id="hashtagType">
              <Translate contentKey="monsterdamChicksApp.hashTag.hashtagType">Hashtag Type</Translate>
            </span>
          </dt>
          <dd>{hashTagEntity.hashtagType}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.hashTag.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hashTagEntity.createdDate ? <TextFormat value={hashTagEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.hashTag.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hashTagEntity.lastModifiedDate ? (
              <TextFormat value={hashTagEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.hashTag.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hashTagEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.hashTag.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hashTagEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.hashTag.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {hashTagEntity.deletedDate ? <TextFormat value={hashTagEntity.deletedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/hash-tag" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hash-tag/${hashTagEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HashTagDetail;
