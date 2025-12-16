import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './help-question.reducer';

export const HelpQuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const helpQuestionEntity = useAppSelector(state => state.helpQuestion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="helpQuestionDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.helpQuestion.detail.title">HelpQuestion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{helpQuestionEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="monsterdamChicksApp.helpQuestion.title">Title</Translate>
            </span>
          </dt>
          <dd>{helpQuestionEntity.title}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="monsterdamChicksApp.helpQuestion.content">Content</Translate>
            </span>
          </dt>
          <dd>{helpQuestionEntity.content}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.helpQuestion.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {helpQuestionEntity.createdDate ? (
              <TextFormat value={helpQuestionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.helpQuestion.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {helpQuestionEntity.lastModifiedDate ? (
              <TextFormat value={helpQuestionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.helpQuestion.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{helpQuestionEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.helpQuestion.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{helpQuestionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.helpQuestion.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {helpQuestionEntity.deletedDate ? (
              <TextFormat value={helpQuestionEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.helpQuestion.subcategory">Subcategory</Translate>
          </dt>
          <dd>{helpQuestionEntity.subcategory ? helpQuestionEntity.subcategory.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/help-question" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/help-question/${helpQuestionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HelpQuestionDetail;
