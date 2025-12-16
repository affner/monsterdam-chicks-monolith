import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { FeedbackType } from 'app/shared/model/enumerations/feedback-type.model';
import { createEntity, getEntity, reset, updateEntity } from './feedback.reducer';

export const FeedbackUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.userLite.entities);
  const feedbackEntity = useAppSelector(state => state.feedback.entity);
  const loading = useAppSelector(state => state.feedback.loading);
  const updating = useAppSelector(state => state.feedback.updating);
  const updateSuccess = useAppSelector(state => state.feedback.updateSuccess);
  const feedbackTypeValues = Object.keys(FeedbackType);

  const handleClose = () => {
    navigate(`/feedback${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserLites({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.feedbackDate = convertDateTimeToServer(values.feedbackDate);
    if (values.feedbackRating !== undefined && typeof values.feedbackRating !== 'number') {
      values.feedbackRating = Number(values.feedbackRating);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...feedbackEntity,
      ...values,
      creator: userLites.find(it => it.id.toString() === values.creator?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          feedbackDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          feedbackType: 'ERROR',
          ...feedbackEntity,
          feedbackDate: convertDateTimeFromServer(feedbackEntity.feedbackDate),
          createdDate: convertDateTimeFromServer(feedbackEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(feedbackEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(feedbackEntity.deletedDate),
          creator: feedbackEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.feedback.home.createOrEditLabel" data-cy="FeedbackCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.feedback.home.createOrEditLabel">Create or edit a Feedback</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="feedback-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.feedback.content')}
                id="feedback-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.feedback.feedbackDate')}
                id="feedback-feedbackDate"
                name="feedbackDate"
                data-cy="feedbackDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.feedback.feedbackRating')}
                id="feedback-feedbackRating"
                name="feedbackRating"
                data-cy="feedbackRating"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.feedback.feedbackType')}
                id="feedback-feedbackType"
                name="feedbackType"
                data-cy="feedbackType"
                type="select"
              >
                {feedbackTypeValues.map(feedbackType => (
                  <option value={feedbackType} key={feedbackType}>
                    {translate(`monsterdamChicksApp.FeedbackType.${feedbackType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.feedback.createdDate')}
                id="feedback-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.feedback.lastModifiedDate')}
                id="feedback-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.feedback.createdBy')}
                id="feedback-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.feedback.lastModifiedBy')}
                id="feedback-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.feedback.deletedDate')}
                id="feedback-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="feedback-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.feedback.creator')}
                type="select"
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/feedback" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FeedbackUpdate;
