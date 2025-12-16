import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './help-related-article.reducer';

export const HelpRelatedArticleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const helpRelatedArticleEntity = useAppSelector(state => state.helpRelatedArticle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="helpRelatedArticleDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.helpRelatedArticle.detail.title">HelpRelatedArticle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{helpRelatedArticleEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="monsterdamChicksApp.helpRelatedArticle.title">Title</Translate>
            </span>
          </dt>
          <dd>{helpRelatedArticleEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="monsterdamChicksApp.helpRelatedArticle.content">Content</Translate>
            </span>
          </dt>
          <dd>{helpRelatedArticleEntity.content}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.helpRelatedArticle.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {helpRelatedArticleEntity.createdDate ? (
              <TextFormat value={helpRelatedArticleEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.helpRelatedArticle.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {helpRelatedArticleEntity.lastModifiedDate ? (
              <TextFormat value={helpRelatedArticleEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.helpRelatedArticle.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{helpRelatedArticleEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.helpRelatedArticle.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{helpRelatedArticleEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.helpRelatedArticle.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {helpRelatedArticleEntity.deletedDate ? (
              <TextFormat value={helpRelatedArticleEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/help-related-article" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/help-related-article/${helpRelatedArticleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HelpRelatedArticleDetail;
