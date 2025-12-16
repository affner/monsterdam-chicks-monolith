import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { LikeEntityKind } from 'app/shared/model/enumerations/like-entity-kind.model';
import { createEntity, getEntity, reset, updateEntity } from './like-mark.reducer';

export const LikeMarkUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.userLite.entities);
  const likeMarkEntity = useAppSelector(state => state.likeMark.entity);
  const loading = useAppSelector(state => state.likeMark.loading);
  const updating = useAppSelector(state => state.likeMark.updating);
  const updateSuccess = useAppSelector(state => state.likeMark.updateSuccess);
  const likeEntityKindValues = Object.keys(LikeEntityKind);

  const handleClose = () => {
    navigate(`/like-mark${location.search}`);
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);
    if (values.multimediaId !== undefined && typeof values.multimediaId !== 'number') {
      values.multimediaId = Number(values.multimediaId);
    }
    if (values.messageId !== undefined && typeof values.messageId !== 'number') {
      values.messageId = Number(values.messageId);
    }
    if (values.postId !== undefined && typeof values.postId !== 'number') {
      values.postId = Number(values.postId);
    }
    if (values.commentId !== undefined && typeof values.commentId !== 'number') {
      values.commentId = Number(values.commentId);
    }

    const entity = {
      ...likeMarkEntity,
      ...values,
      liker: userLites.find(it => it.id.toString() === values.liker?.toString()),
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          entityType: 'VIDEO',
          ...likeMarkEntity,
          createdDate: convertDateTimeFromServer(likeMarkEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(likeMarkEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(likeMarkEntity.deletedDate),
          liker: likeMarkEntity?.liker?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.likeMark.home.createOrEditLabel" data-cy="LikeMarkCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.likeMark.home.createOrEditLabel">Create or edit a LikeMark</Translate>
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
                  id="like-mark-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.entityType')}
                id="like-mark-entityType"
                name="entityType"
                data-cy="entityType"
                type="select"
              >
                {likeEntityKindValues.map(likeEntityKind => (
                  <option value={likeEntityKind} key={likeEntityKind}>
                    {translate(`monsterdamChicksApp.LikeEntityKind.${likeEntityKind}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.createdDate')}
                id="like-mark-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.lastModifiedDate')}
                id="like-mark-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.createdBy')}
                id="like-mark-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.lastModifiedBy')}
                id="like-mark-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.deletedDate')}
                id="like-mark-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.multimediaId')}
                id="like-mark-multimediaId"
                name="multimediaId"
                data-cy="multimediaId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.messageId')}
                id="like-mark-messageId"
                name="messageId"
                data-cy="messageId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.postId')}
                id="like-mark-postId"
                name="postId"
                data-cy="postId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.likeMark.commentId')}
                id="like-mark-commentId"
                name="commentId"
                data-cy="commentId"
                type="text"
              />
              <ValidatedField
                id="like-mark-liker"
                name="liker"
                data-cy="liker"
                label={translate('monsterdamChicksApp.likeMark.liker')}
                type="select"
                required
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
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/like-mark" replace color="info">
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

export default LikeMarkUpdate;
